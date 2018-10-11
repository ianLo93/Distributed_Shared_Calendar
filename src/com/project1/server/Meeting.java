package com.project1.server;

import java.io.*;
import java.util.Comparator;

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

    public static Comparator<Meeting> timeComparator = new Comparator<Meeting>() {
        @Override
        public int compare(Meeting m1, Meeting m2) {
            int lex1 = m1.getDay().compareTo(m2.getDay());
            if (lex1 != 0) return lex1;
            int lex2 = m1.getStartTime().compareTo(m2.getStartTime());
            if (lex2 != 0) return lex2;
            return m1.getName().compareTo(m2.getName());
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Meeting other = (Meeting) obj;
        if (!this.name.equals(other.name)) return false;
        else return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89*hash + (this.name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString(){
        StringBuilder m = new StringBuilder(name + " " + day+ " " + start + " " + end + " ");
        int i=0;
        for (; i<participants.length-1; i++){
            m.append(participants[i]+",");
        }
        m.append(participants[i]);
        return m.toString();
    }

//    public static void main(String[] args) {
//        String[] p = {"localhost"};
//        Meeting m = new Meeting("abs", "10/9/2018", "8:00", "13:30", p);
//        m.printMeeting();
//    }

}
