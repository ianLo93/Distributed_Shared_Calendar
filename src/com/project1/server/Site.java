package com.project1.server;

import com.project1.app.Calendar;
import com.project1.client.Client;
import com.project1.client.Message;

import java.io.*;
import java.util.*;


public class Site {

    private String siteid;
    private int port;
    private int counter;
    private List<Meeting> schedule;
    private List<Event> log;
    private List<Event> plog;
    private int[][] T;

    @SuppressWarnings("unchecked")
    public Site(String siteid_, int port_) {
        try {
            FileInputStream saveFile = new FileInputStream("state.sav");
            ObjectInputStream restore = new ObjectInputStream(saveFile);
            siteid = (String) restore.readObject();
            port = (Integer) restore.readObject();
            counter = (Integer) restore.readObject();
            schedule = (ArrayList<Meeting>) restore.readObject();
            log = (ArrayList<Event>) restore.readObject();
            plog = (ArrayList<Event>) restore.readObject();
            T = (int[][]) restore.readObject();
            restore.close();
        } catch (IOException i) {
            init(siteid_, port_);
        } catch (ClassNotFoundException c) {
            init(siteid_, port_);
        }
    }
    public void view() {
        Collections.sort(schedule, Meeting.timeComparator);
        for (Meeting m : schedule) System.out.println(m);
    }

    public void myView() {
        ArrayList<Meeting> ms = relevantMeetings(new String[]{siteid});
        Collections.sort(ms, Meeting.timeComparator);
        for (Meeting m : ms) System.out.println(m);
    }

    public void viewLog() {
        for (Event e : log) System.out.println(e);
    }

    public boolean hasConflict(String day, String start, String end, String[] participants) {
        ArrayList<Meeting> meetings = relevantMeetings(participants);
        int[] occupiedTimes = new int[48];
        int s = parse_time(start), e = parse_time(end);
        for (Meeting m : meetings) {
            if (!m.getDay().equals(day)) continue;
            int sm = parse_time(m.getStartTime()), em = parse_time(m.getEndTime());
            for (int i = sm; i <= em; i++) occupiedTimes[i] = 1;
        }
        for (int i = s; i <= e; i++) {
            if (occupiedTimes[i] == 1) return true;
        }
        return false;
    }

    public void save_state() {
        try {
            FileOutputStream saveFile = new FileOutputStream("state.sav");
            ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(siteid);
            save.writeObject(port);
            save.writeObject(counter);
            save.writeObject(schedule);
            save.writeObject(log);
            save.writeObject(plog);
            save.writeObject(T);
            save.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public void addMeeting(Meeting m) {
        counter = counter + 1;
        schedule.add(m);
        Event e = new Event("create", counter, siteid, m);
        updateT();
        log.add(e);
        plog.add(e);
    }

    public void updateT() {
        int i = Calendar.phonebook.get(siteid).getKey();
        T[i][i] = counter;
    }

    public void rmMeeting(Meeting m) {
        counter = counter + 1;
        schedule.remove(m);
        int i = Calendar.phonebook.get(siteid).getKey();
        T[i][i] = counter;
        Event e = new Event("cancel", counter, siteid, m);
        log.add(e);
        plog.add(e);
    }

    public void sendEvent(Message msg) {
        // Create a client to send msg
        Client client = new Client(siteid, port);
        String[] participants = msg.getMeeting().getParticipants();
        // Insert T to message
        msg.setT(T);
        // Client send message to participants
        for (String p: participants) {
            // Make NP and insert to message
            List<Event> NP = makeNP(p);
            msg.setNP(NP);
            int port = Calendar.phonebook.get(p).getValue();
            client.sendMsg(msg, p, port);
        }
        client.close();
    }

    public Meeting getMeeting(String name_) {
        for (Meeting m: schedule) {
            if (m.getName().equals(name_)) return m;
        }
        return null;
    }

    public List<Event> makeNP(String sitej) {
        List<Event> NP = new ArrayList<>();
        for (Event e: plog) {
            if (!hasRec(e, sitej)) NP.add(e);
        }
        return NP;
    }


    private void init(String siteid_, int port_) {
        int s = Calendar.phonebook.size();
        this.counter = 0;
        this.siteid = siteid_;
        this.port = port_;

        this.T = new int[s][s];
        this.log = new ArrayList<>();
        this.plog = new ArrayList<>();
        this.schedule = new ArrayList<>();
    }

    private ArrayList<Meeting> relevantMeetings(String[] participants) {
        ArrayList<Meeting> meetings = new ArrayList<>();
        HashSet<String> dict = new HashSet<>(Arrays.asList(participants));
        for (Meeting m : schedule) {
            for (String p : m.getParticipants()) {
                if (dict.contains(p)) {
                    meetings.add(m);
                    break;
                }
            }
        }
        return meetings;
    }

    private int parse_time(String timestamp) {
        String[] clocks = timestamp.split(":");
        if (clocks.length != 2) {
            System.out.println("ERROR: Invalid Time");
            System.out.println("USAGE: <hour[1:24]>:<minute>[00/30]");
            return -1;
        }
        int first = Integer.parseInt(clocks[0]);
        int second = Integer.parseInt(clocks[1]) / 30;
        return first * 2 + second;
    }


    public Event [] makeNE(Event [] NP){
        ArrayList<Event> ne = new ArrayList<Event>();
        for (Event e : NP){
            if (!hasRec(e, this.siteid)){
                ne.add(e);
            }
        }
        Object NE = ne.toArray();
        return (Event[]) NE;
//        int i = 0;
//        for (Event e : ne){
//            NE[i] = e;
//            i++;
//        }
//        return NE;

    }

    public void updateT(Message msg){
        int i = Calendar.phonebook.get(siteid).getKey();

        if (!msg.getSender().equals(siteid)){
            int k = Calendar.phonebook.get(msg.getSender()).getKey();

            int [][] Tk = msg.getT();

            for (int r = 0; r < T.length; r++){
                for (int s = 0; s < T.length; s++){
                    T[r][s] = Integer.max(T[r][s], Tk[r][s]);
                }
            }

            for (int r = 0; r < T.length; r++){
                T[i][r] = Integer.max(T[i][r], Tk[k][r]);
            }
        }
        else {
            T[i][i] = counter;
        }
    }

    public void updatePL(Event [] NE){
        for (int i = 0; i < NE.length; i++)
            plog.add(NE[i]);

        int i = Calendar.phonebook.get(siteid).getKey();
        for (Event e : plog){
            boolean allKnow = true;
            for (int k = 0; k < T.length; i++){
                if (T[i][k] < e.getTime()) allKnow = false;
            }
            if (allKnow) plog.remove(e);
        }
    }


    public void UpdateSchedule(Event [] NE){
        for (int i = 0; i < NE.length; i++){
            Event e = NE[i];
            if (e.getOp().equals("create")){
                insert(e.getMeeting());
            }
            else if (e.getOp().equals("cancel")){
                delete(e.getMeeting());
            }
            counter = Integer.max(counter, e.getTime());
        }

    }

    public Event [] handleConflict(){
        int [][] timeline = new int[7][48];
        Arrays.fill(timeline, 0);

        ArrayList<Meeting> sortedMeeting = schedule;
        Collections.sort(sortedMeeting, new MeetingCompare());

        ArrayList<Event> toCancel = new ArrayList<Event>();

        for (Meeting m : sortedMeeting){
            int s = parse_time(m.getStartTime());
            int e = parse_time(m.getEndTime());
            String day = m.getDay().toLowerCase();
            int d;
            switch (day) {
                case "sunday":
                    d = 0;
                    break;
                case "monday":
                    d = 1;
                    break;
                case "tuesday":
                    d = 2;
                    break;
                case "wednesday":
                    d = 3;
                    break;
                case "thursday":
                    d = 4;
                    break;
                case "friday":
                    d = 5;
                    break;
                case "saturday":
                    d = 6;
                    break;
                default:
                    d = -1;
                    break;
            }
            for (int t = s; t <= e; t++){
                if(timeline[d][t] == 1){
                    // TODO: collect and delete these meetings
                    delete(m);
                    Event event = new Event("cancel", counter, siteid, m);
                    toCancel.add(event);
                    plog.add(event);
                    int i = Calendar.phonebook.get(siteid).getKey();
                    T[i][i] = counter;

                }
                else timeline[d][t] = 1;
            }


        }
        Event [] cancelEvents = new Event[toCancel.size()];
        return cancelEvents;
    }

    private void delete(Meeting m){
        schedule.remove(m);
        counter += 1;
    }
    private void insert(Meeting m){
        schedule.add(m);
        counter += 1;
    }

    private boolean hasRec(Event e, String sitej) {
        try {
            int k = Calendar.phonebook.get(e.getSite()).getKey();
            int j = Calendar.phonebook.get(sitej).getKey();
            return T[j][k] >= e.getTime();
        } catch (NullPointerException n) {
            System.out.println("ERROR: No such user");
            return true;
        }

    }
}
