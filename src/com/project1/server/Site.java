package com.project1.server;

import com.project1.app.Calendar;
import com.project1.client.Client;
import com.project1.client.Message;

import java.io.*;
import java.text.ParseException;
import java.util.*;


public class Site {

    private String siteid;
    private int port;
    private int counter;
    private ArrayList<Meeting> schedule;
    private ArrayList<Event> log;
    private ArrayList<Event> plog;
    private int[][] T;

    @SuppressWarnings("unchecked")
    public Site(String siteid_, int port_) {
        try {
            FileInputStream saveFile = new FileInputStream("state.sav");
            ObjectInputStream restore = new ObjectInputStream(saveFile);
            this.siteid = (String) restore.readObject();
            this.port = (Integer) restore.readObject();
            this.counter = (Integer) restore.readObject();
            this.schedule = (ArrayList<Meeting>) restore.readObject();
            this.log = (ArrayList<Event>) restore.readObject();
            this.plog = (ArrayList<Event>) restore.readObject();
            this.T = (int[][]) restore.readObject();
            restore.close();
        } catch (Exception i) {
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
            for (int i = sm; i < em; i++) occupiedTimes[i] = 1;
        }
        for (int i = s; i < e; i++) {
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

    public void addMeeting(Meeting m, boolean hasEvent) {
        counter = counter + 1;
        schedule.add(m);
        updateT(null);
        if (!hasEvent) {
            Event e = new Event("create", counter, siteid, m);
            log.add(e);
            plog.add(e);
            System.out.println("Meeting "+m.getName()+" scheduled");
        }
    }

    public void rmMeeting(Meeting m, boolean hasEvent) {
        counter = counter + 1;
        schedule.remove(m);
        updateT(null);
        if (!hasEvent) {
            Event e = new Event("cancel", counter, siteid, m);
            log.add(e);
            plog.add(e);
            System.out.println("Meeting "+m.getName()+" cancelled");
        }
    }

    public void sendMessage(Message msg) {
        // Create a client to send msg
        Client client = new Client(siteid, port);
        String[] participants = msg.getMeeting().getParticipants();

        // Insert T to message
        msg.setT(T);
        // Client send message to participants
        for (String p: participants) {
            // Make NP and insert to message
            if (!p.equals(siteid)) {
                ArrayList<Event> NP = makeNP(p);
                msg.setNP(NP);
                int port = Calendar.phonebook.get(p)[1];
                client.sendMsg(msg, p, port);
            }
        }
        client.close();
    }

    public Meeting getMeetingByName(String name_) { // Get meeting by meeting name
        for (Meeting m: schedule) {
            if (m.getName().equals(name_)) return m;
        }
        return null;
    }

    public ArrayList<Event> makeNP(String sitej) {
        ArrayList<Event> NP = new ArrayList<>();
        for (Event e: plog) {
            if (!hasRec(e, sitej)) NP.add(e);
        }
        return NP;
    }

    public ArrayList<Event> makeNE(ArrayList<Event> NP){
        ArrayList<Event> NE = new ArrayList<>();
        for (Event e : NP){
            if (!hasRec(e, this.siteid)){
                NE.add(e);
            }
        }
        return NE;
    }

    public void update(Message msg){
        ArrayList<Event> NP = msg.getNP();
        ArrayList<Event> NE = this.makeNE(NP);
        // update Meetings
        this.updateSchedule(NE);
        this.updateT(msg);
        this.updateLog(NE);
    }

    public void init(String siteid_, int port_) {
        this.counter = 0;
        this.siteid = siteid_;
        this.port = port_;

        int s = Calendar.phonebook.size();
        this.T = new int[s][s];
        this.log = new ArrayList<>();
        this.plog = new ArrayList<>();
        this.schedule = new ArrayList<>();
    }

    public static int parse_time(String timestamp) {
        String[] clocks = timestamp.split(":");
        if (clocks.length != 2) {
            System.out.println("ERROR: Invalid Time");
            System.out.println("USAGE: <hour[1:24]>:<minute>[00/30]");
            return -1;
        }
        try {
            int first = Integer.parseInt(clocks[0]);
            int second = Integer.parseInt(clocks[1]);
            return first * 2 + second/30;
        } catch (NumberFormatException n) {
            return -1;
        }
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

    private void updateT(Message msg){
        int i = Calendar.phonebook.get(siteid)[0];
        if (msg != null){
            int k = Calendar.phonebook.get(msg.getSender())[0];
            // Get T matrix from site K
            int[][] Tk = msg.getT();
            // Update global knowledge
            for (int r = 0; r < T.length; r++){
                for (int s = 0; s < T.length; s++){
                    T[r][s] = Integer.max(T[r][s], Tk[r][s]);
                }
            }
            for (int r = 0; r < T.length; r++){
                T[i][r] = Integer.max(T[i][r], Tk[k][r]);
            }
        }
        else { // Sent from my site
            T[i][i] = counter;
        }
    }

    private void updateLog(ArrayList<Event> NE){
        for (Event e : NE){
            plog.add(e);
            log.add(e);
        }
        // Update partial log, remove globally known logs
        for (int i = 0; i < plog.size(); i++){
            Event e = plog.get(i);
            boolean allKnow = true;
            for (String sitej: Calendar.phonebook.keySet()) {
                if (!hasRec(e, sitej)) {allKnow = false; break;}
            }
            if (allKnow) {plog.remove(i); i--;}
        }
    }


    private void updateSchedule(ArrayList<Event> NE){
        // Reproduce the log, if the meeting was created, we create
        // if the meeting was cancelled we cancelled
        // We can use HashMap to accelerate the process in large data input
        for (Event e : NE){
            if (e.getOp().equals("create")){
                addMeeting(e.getMeeting(), true);
            }
            if (e.getOp().equals("cancel")){
                rmMeeting(e.getMeeting(), true);
            }
            counter = Integer.max(counter, e.getTime());
        }
    }

    public void handle_conflict(){
        // 2-D time Vec
        int[][] timeline = new int[7][48];

        // Get relevant meetings
        ArrayList<Meeting> sortedMeeting = relevantMeetings(new String[]{siteid});
        Collections.sort(sortedMeeting, new MeetingCompare());

        for (int i = 0; i < sortedMeeting.size(); i++) {
            Meeting m = sortedMeeting.get(i);
            int s = parse_time(m.getStartTime());
            int e = parse_time(m.getEndTime());
            String day = m.getDay();
            // Instead of switch cases, we can use HashMap<>
            int d = -1;
            switch (day) {
                case "10/14/2018":
                    d = 0;
                    break;
                case "10/15/2018":
                    d = 1;
                    break;
                case "10/16/2018":
                    d = 2;
                    break;
                case "10/17/2018":
                    d = 3;
                    break;
                case "10/18/2018":
                    d = 4;
                    break;
                case "10/19/2018":
                    d = 5;
                    break;
                case "10/20/2018":
                    d = 6;
                    break;
            }
            // Check conflicts
            boolean conflict = false;
            for (int t = s; t < e; t++) {
                if (timeline[d][t] == 1) {
                    conflict = true;
                    if (getMeetingByName(m.getName()) != null) rmMeeting(m, false);
                    Message msg = new Message("cancel", null, T, siteid, m);
                    sendMessage(msg);
                    break;
                }
            }
            // If we don't have conflict for meeting m
            if (!conflict){
                for (int t = s; t < e; t++) timeline[d][t] = 1;
            }
        }
    }

    private boolean hasRec(Event e, String sitej) {
        try {
            int k = Calendar.phonebook.get(e.getSite())[0];
            int j = Calendar.phonebook.get(sitej)[0];
            return T[j][k] >= e.getTime();
        } catch (NullPointerException n) {
            System.out.println("ERROR: No such user");
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\nSITE INFO\nID: "
                +siteid+"\nPort: "+port+"\nCounter: "+counter+"\nGlobal Knowledge:\n");
        for (int i=0; i<T.length; i++) {
            for (int j=0; j<T.length; j++) s.append(T[i][j]+" ");
            s.append("\n");
        }
        return s.toString();
    }
}
