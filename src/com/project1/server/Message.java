package com.project1.server;

import java.util.*;

public class Message {

    private String msg;
    private String[] NP;
    private int[][] T;
    private int sender;
    private Meeting meeting;

    public Message(String msg_, String[] NP_, int[][] T_, int sender_) {
        this.msg = msg_;
        this.NP = NP_;
        this.T = T_;
        this.sender = sender_;
    }

    public Message(String msg_, String[] NP_, int[][] T_, int sender_,
                   String name_, String day_, String start_, String end_, String[] participants_) {
        this.msg = msg_;
        this.NP = NP_;
        this.T = T_;
        this.sender = sender_;
        meeting = new Meeting(name_, day_, start_, end_, participants_);
    }

    public String getMsg() { return msg; }
    public String[] getNP() { return NP; }
    public int[][] getT() { return T; }
    public int getSender() { return sender; }
    public Meeting getMeeting() { return meeting; }

}
