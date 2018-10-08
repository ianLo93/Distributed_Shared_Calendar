package com.project1.client;
import java.util.*;
import com.project1.meeting.Meeting;


public class Client {

    public String getSite_id() {
        return site_id;
    }



    String site_id;

    ArrayList<Meeting> events;

    ArrayList<String> T;

    ArrayList<Integer> avaible;




    public Client(String id, Integer n){
        this.site_id = id;
        this.avaible = new ArrayList<Integer>(Collections.nCopies(60, 0));
        char[] chars = new char[n];
        Arrays.fill(chars, '0');
        String s = new String(chars);
        this.T = new ArrayList<String>(Collections(n), s);
    }




    public void printit(){ System.out.println("My id is " + site_id); }

}