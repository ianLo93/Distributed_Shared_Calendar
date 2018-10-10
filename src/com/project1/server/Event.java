package com.project1.server;

public class Event {

    private String op;
    private int time;
    private String site;
    private Meeting meeting;

    public Event(String op_, int t_, String site_, Meeting m_) {
        this.op = op_;
        this.time = t_;
        this.site = site_;
        this.meeting = m_;
    }

    public String getOp() {
        return op;
    }

    public int getTime() {
        return time;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public String getSite() {
        return site;
    }

    public void printEvent() {
        System.out.print(op + " ");
        meeting.printMeeting();
    }

//    public static void main(String[] args) {
//        Meeting m = new Meeting(
//                "Breakfast", "10/10/2018", "8:00", "13:30", new String[] {"me"});
//        Event e = new Event("create", 2, "aaa", m);
//        e.printEvent();
//    }
}
