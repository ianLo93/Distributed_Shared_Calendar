package com.project1.app;
import java.io.*;
import java.util.*;
import com.project1.meeting.Meeting;
import com.project1.client.Client;
import com.project1.server.Server;
import javafx.util.Pair;

public class MyApp {

    private String site_id;

    private String port;

    private int C;

    private ArrayList<Meeting> schedule;

    private Pair<String, String> log;

    private Pair<String, String> plog;

    private ArrayList<String> T;

    private ArrayList<Integer> available;

    private Map<String, Integer> mtoT;


    public void readFile(String path){
        try {
            File file = new File(path);
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String line = "";

            int index = 0;
            mtoT = new HashMap<>();
            while ( (line = buffer.readLine()) != null ) {
                System.out.println(line);
                line = line.trim();
                String [] words = line.split(" ");
                mtoT.put(words[0], index);
                index++;

            }
        }
        catch (IOException i) {
            System.out.println(i);
        }


    }

    public void init(String id, String portNum){
        this.C = 0;
        this.site_id = id;
        this.port = portNum;
        this.available = new ArrayList<Integer>(Collections.nCopies(60, 0));

        char[] chars = new char[3];
        Arrays.fill(chars, '0');
        String s = new String(chars);
        this.T = new ArrayList<String>(Collections.nCopies(3, s));
        this.mtoT = new HashMap<>();
//        this.log = new Pair<>();
//        this.plog = new Pair<>();
    }




    public static void main(String args[]) throws Exception {

//    if(args.length < 1){
//        System.out.println("ERROR: Please include 1 argument");
//        System.exit(1);
//    }


        Server server = new Server(5000);
        server.setDaemon(true);
        server.start();

        Client client = new Client();

        Scanner sc = new Scanner(System.in);
        String line = "";
        while (!line.equals("end")) {
            line = sc.nextLine();
            String msg = client.sendMsg(line, 5000);
            System.out.println(msg);
        }

        client.close();

    }

}