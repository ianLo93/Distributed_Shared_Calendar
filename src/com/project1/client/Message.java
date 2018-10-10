package com.project1.client;

import com.project1.server.Event;
import com.project1.server.Meeting;

import java.io.*;

public class Message implements Serializable {

    private String msg;
    private Event[] NP; // Partial Log
    private int[][] T; // Global knowledge matrix
    private String sender;
    private Meeting meeting;

    public Message(String msg_, Event[] NP_, int[][] T_, String sender_,
                   String name_, String day_, String start_, String end_, String[] participants_) {
        this.msg = msg_;
        this.NP = NP_;
        this.T = T_;
        this.sender = sender_;
        this.meeting = new Meeting(name_, day_, start_, end_, participants_);
    }

    public String getMsg() { return msg; }
    public Event[] getNP() { return NP; }
    public int[][] getT() { return T; }
    public String getSender() { return sender; }
    public Meeting getMeeting() { return meeting; }

}
