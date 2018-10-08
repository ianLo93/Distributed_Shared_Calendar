package com.project1.app;
import java.io.*;
import java.util.*;
import com.project1.client.Client;
import com.project1.server.Server;
import javafx.util.Pair;

public class MyApp {


//    public void readFile(String path){
//        try {
//            File file = new File(path);
//            BufferedReader buffer = new BufferedReader(new FileReader(file));
//            String line = "";
//
//            int index = 0;
//            mtoT = new HashMap<>();
//            while ( (line = buffer.readLine()) != null ) {
//                System.out.println(line);
//                line = line.trim();
//                String [] words = line.split(" ");
//                mtoT.put(words[0], index);
//                index++;
//
//            }
//        }
//        catch (IOException i) {
//            System.out.println(i);
//        }
//
//
//    }






    public static void main(String args[]) throws Exception {

        if(args.length != 1){
            System.out.println("ERROR: Invalid Arguments");
            System.out.println("USAGE: ./a.out <site_id> ");
            System.exit(1);
        }

        Map<Integer, Pair<String, Integer>> phonebook = new HashMap<>();
        phonebook.put(0, new Pair("localhost", 5000));
        phonebook.put(1, new Pair("127.4.0.2", 5001));
        phonebook.put(2, new Pair("127.4.0.3", 5002));

        int port = phonebook.get(Integer.parseInt(args[1])).getValue();
        String siteid = phonebook.get(Integer.parseInt(args[1])).getKey();

        Site mysite = new Site(siteid, port);
        Server server = new Server(port);
        server.setDaemon(true);
        server.start();

        Client client = new Client();

        Scanner sc = new Scanner(System.in);
        String line = "";
        while (!line.equals("end")) {
            line = sc.nextLine();
            String msg = client.sendMsg(line, 5000);
            System.out.println(msg);

            // execute command
            String [] commands = msg.split(" ");

            if (commands.length < 2) {
                System.out.println("Invalid command");
            }
            else if (commands[1].equals("schedule")){
                if (commands.length != 7 ) System.out.println("Invalid command");

                else if (mysite.checkAvailabilityOfAll(commands[3], commands[4], commands[5], commands[6])){
                    // send invitation to all sites.

                }
                else {
                    System.out.println("Unable to schedule meeting "+ commands[2]);
                }

            }
            else if (commands[1].equals("cancel")){
                // check if meeting exists

                // cancel myself

                // send cancel to all participants
            }
            else if (commands[1].equals("view")){
                mysite.view();

            }
            else if (commands[1].equals("myview")){
                mysite.myView();

            }
            else if (commands[1].equals("log")){
                mysite.viewLog();

            }
            else {
                System.out.println("Invalid command");
            }
        }



        client.close();

    }

}