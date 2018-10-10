package com.project1.server;

import com.project1.app.Calendar;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


public class Site {

    private String siteid;
    private int port;
    private int counter;
    private ArrayList<Meeting> schedule;
    private ArrayList<Event> log;
    private ArrayList<Event> plog;
    private int[][] T;

    public Site(String siteid_, int port_){
//        try {
//            load_state();
//        }
//        catch (IOException i){
            init(siteid_, port_);
//        }
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

    private void load_state() {
        // TODO load state from computer
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
