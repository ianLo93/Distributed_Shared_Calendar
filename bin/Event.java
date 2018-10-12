
import java.io.*;

public class Event implements Serializable {

    private String op;
    private int time;
    private String sitei; // Event happens in site i
    private Meeting meeting;

    public Event(String op_, int t_, String site_, Meeting m_) {
        this.op = op_;
        this.time = t_;
        this.sitei = site_;
        this.meeting = m_;
    }

    public String getOp() { return op; }
    public int getTime() { return time;}
    public Meeting getMeeting() { return meeting; }
    public String getSite() { return sitei; }

    @Override
    public String toString() {
        StringBuilder e = new StringBuilder(op+" ");
        e.append(meeting.toString());
        return e.toString();
    }

//    public static void main(String[] args) {
//        Meeting m = new Meeting(
//                "Breakfast", "10/10/2018", "8:00", "13:30", new String[] {"me", "heheh"});
//        Event e = new Event("create", 2, "aaa", m);
//        System.out.println(e);
//        System.out.println(m);
//    }
}
