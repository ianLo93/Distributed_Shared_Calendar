package com.project1.meeting;
import java.util.ArrayList;

public class Meeting {

    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }



    public String getStart() {
        return start;
    }

    String name;
    String day;
    String start;
    String end;
    ArrayList<String> participants;

    public Meeting(String Name, String Day, String Start, String End, ArrayList<String> Participants){
        this.name = Name;
        this.day = Day;
        this.start = Start;
        this.end = End;
        this.participants = Participants;
        System.out.print("create " + name + day + start + end);
        for (String id : participants){
            System.out.print(id + ",");
        }

    }

    public void printMeeting(){
        System.out.print(name + day + start + end);
        for (String id : participants){
            System.out.print(id + ",");
        }
    }

}
