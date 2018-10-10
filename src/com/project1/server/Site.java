package com.project1.server;

import com.project1.app.Calendar;
import javafx.util.Pair;

import java.io.*;
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
    public Site(String siteid_, int port_){
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
        } catch (IOException i){
            init(siteid_, port_);
        } catch (ClassNotFoundException c) {
            init(siteid_, port_);
        }
    }

    public void view(){
        for (Meeting m: schedule) m.printMeeting();
    }

    public void myView(){
        ArrayList<Meeting> ms = relevantMeetings(new String[] {siteid});
        for (Meeting m: ms) m.printMeeting();
    }

    public void viewLog(){
        for (Event e: log) e.printEvent();
    }

    public boolean hasConflict(String day, String start, String end, String[] participants){
        ArrayList<Meeting> meetings = relevantMeetings(participants);
        int[] occupiedTimes = new int[48];
        int s = parse_time(start), e = parse_time(end);
        for (Meeting m: meetings) {
            if (!m.getDay().equals(day)) continue;
            int sm = parse_time(m.getStartTime()), em = parse_time(m.getEndTime());
            for (int i=sm; i<=em; i++) occupiedTimes[i] = 1;
        }
        for (int i=s; i<= e; i++) {
            if (occupiedTimes[i] == 1) return true;
        }
        return false;
    }

    private void init(String siteid_, int port_){
        int s = Calendar.phonebook.size();
        this.counter = 0;
        this.siteid = siteid_;
        this.port = port_;

        this.T = new int[s][s];
        this.log = new ArrayList<>();
        this.plog = new ArrayList<>();
        this.schedule = new ArrayList<>();
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

    private ArrayList<Meeting> relevantMeetings(String[] participants) {
        ArrayList<Meeting> meetings = new ArrayList<>();
        HashSet<String> dict = new HashSet<>(Arrays.asList(participants));
        for (Meeting m: schedule) {
            for (String p: m.getParticipants()) {
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
        int second = Integer.parseInt(clocks[1]);
        return first*2+second;
    }

    private boolean hasRec() {
        //TODO
        return true;
    }

}
