package com.project1.app;
import java.util.*;
import com.project1.client.Client;
import com.project1.client.Message;
import com.project1.server.Server;
import javafx.util.*;

public class Calendar {

    public static Map<String, Pair<Integer, Integer>> phonebook = new HashMap<>();

    public static void main(String args[]) {

        if(args.length != 1){
            System.out.println("ERROR: Invalid Arguments");
            System.out.println("USAGE: ./a.java <site_id> ");
            System.exit(1);
        }

        phonebook.put("localhost", new Pair(0, 5000));
        phonebook.put("127.4.0.2", new Pair(1, 5001));
        phonebook.put("127.4.0.3", new Pair(2, 5002));

        String siteid = args[0];
        int port = Calendar.phonebook.get(siteid).getValue();

        Server server = new Server(siteid, port);
        server.setDaemon(true);
        server.start();

        Client client = new Client(siteid, port);

        Scanner sc = new Scanner(System.in);
        String command = "";
        while (!command.equals("% quit")) {
            command = sc.nextLine();
            Message msg = client.parse_command(command);
            client.sendMsg(msg, siteid, port);
        }

        client.close();
    }

    //  public void readFile(String path){
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

}