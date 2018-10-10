package com.project1.server;

import java.io.*;
import java.util.ArrayList;

public class Meeting implements Serializable {

    private String name;
    private String day;
    private String start;
    private String end;
    private String[] participants;

    public Meeting(String name_, String day_, String start_, String end_, String[] participants_){
        this.name = name_;
        this.day = day_;
        this.start = start_;
        this.end = end_;
        this.participants = participants_;
    }

    public String getName() { return name; }
    public String getDay() { return day; }
    public String getStartTime() { return start; }
    public String getEndTime() { return end; }
    public String[] getParticipants() { return participants; }

    public void printMeeting(){
        System.out.print(name + " " + day+ " " + start + " " + end + " ");
        for (String p : participants){
            System.out.print(p + ",");
        }
    }

    public static void main(String[] args) {
        String[] p = {"localhost"};
        Meeting m = new Meeting("abs", "10/9/2018", "8:00", "13:30", p);
        m.printMeeting();
    }

}
