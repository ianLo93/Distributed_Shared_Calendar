package com.project1.server;

import com.project1.app.Calendar;
import com.project1.client.Message;
import javafx.util.Pair;

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

    public int[][] getT() { return T; }

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

    public Meeting getMeeting(String name_) {
        for (Meeting m: schedule) {
            if (m.getName().equals(name_)) return m;
        }
        return null;
    }

    public Event[] makeNP(String sitej) {
        ArrayList<Event> np = new ArrayList<>();
        for (Event e: plog) {
            if (!hasRec(e, sitej)) np.add(e);
        }
        Object NP = np.toArray();
        return (Event[]) NP;
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

    private boolean hasRec(Event e, String sitej) {
        int k = Calendar.phonebook.get(e.getSite()).getKey();
        int j = Calendar.phonebook.get(sitej).getKey();
        return T[j][k] >= e.getTime();
    }
}