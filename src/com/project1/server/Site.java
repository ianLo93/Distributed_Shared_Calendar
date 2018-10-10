package com.project1.server;

import javafx.util.Pair;
import java.util.*;


public class Site {

    private String siteid;
    private int port;
    private int counter;
    private ArrayList<Meeting> schedule;
    private ArrayList<Pair<String, String>> log;
    private ArrayList<Pair<String, String>> plog;
    private ArrayList<String> T;
//    private ArrayList<Integer> timeline;
    private Map<String, Integer> mtoT;

    public Site(String siteid_, int port_){
        init(siteid_, port_);
    }

    private void init(String siteid_, int port_){
        this.counter = 0;
        this.siteid = siteid_;
        this.port = port_;
//        this.timeline = new ArrayList<Integer>(Collections.nCopies(48, 0));

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
}
