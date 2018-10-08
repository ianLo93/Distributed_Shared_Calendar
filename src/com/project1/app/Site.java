package com.project1.app;

import com.project1.meeting.Meeting;
import javafx.util.Pair;
import java.util.*;


public class Site {

    public String site_id;

    public int port;

    public int counter;

    public ArrayList<Meeting> schedule;

    public ArrayList<Pair<String, String>> log;

    public ArrayList<Pair<String, String>> plog;

    public ArrayList<String> T;

    public ArrayList<Integer> timeline;

    public Map<String, Integer> mtoT;

    private void init(String id, int portNum){
        this.counter = 0;
        this.site_id = id;
        this.port = portNum;
        this.timeline = new ArrayList<Integer>(Collections.nCopies(48, 0));

        char[] chars = new char[3];
        Arrays.fill(chars, '0');
        String s = new String(chars);
        this.T = new ArrayList<String>(Collections.nCopies(3, s));
        this.mtoT = new HashMap<>();
        this.log = new ArrayList<Pair<String, String>>();
        this.plog = new ArrayList<Pair<String, String>>();
        this.schedule = new ArrayList<Meeting>();
    }

    public boolean checkAvailabilityOfAll(String day, String start, String end, String participants){
        String [] parts = participants.split(",");
        for (String id : parts){
            // TODO
        }
        return true;


    }

    public boolean checkAvailability(String day, String start, String end){
        // TODO
        return true;
    }

    public void view(){

    }
    public void myView(){}

    public void viewLog(){

    }


    public Site(String id, int portNum){
        init(id, portNum);
    }
}
