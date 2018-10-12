
import java.io.*;
import java.util.*;

public class Message implements Serializable {

    private String msg;
    private ArrayList<Event> NP; // Partial Log
    private int[][] T; // Global knowledge matrix
    private String sender;
    private Meeting meeting;

    public Message(String msg_, ArrayList<Event> NP_, int[][] T_, String sender_,
                   Meeting m_) {
        this.msg = msg_;
        this.NP = NP_;
        this.T = T_;
        this.sender = sender_;
        this.meeting = m_;
    }

    public Message(String msg_, ArrayList<Event> NP_, int[][] T_, String sender_,
                   String name_, String day_, String start_, String end_, String[] participants_) {
        this.msg = msg_;
        this.NP = NP_;
        this.T = T_;
        this.sender = sender_;
        this.meeting = new Meeting(name_, day_, start_, end_, participants_);
    }

    public String getMsg() { return msg; }
    public ArrayList<Event> getNP() { return NP; }
    public int[][] getT() { return T; }
    public String getSender() { return sender; }
    public Meeting getMeeting() { return meeting; }

    public void setNP(ArrayList<Event> NP_) { this.NP = NP_; }
    public void setT(int[][] T_) { this.T = T_; }
    public void setMeeting(Meeting m_) { this.meeting= m_; }
}
