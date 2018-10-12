package com.project1.app;
import java.util.*;
import java.io.*;
import com.project1.client.Client;
import com.project1.client.Message;
import com.project1.server.Server;

public class Calendar {

    public static HashMap<String, int[]> phonebook;

    public static void readFile(String path) {
        try {
            File file = new File(path);
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String line;

            int index = 0;
            phonebook = new HashMap<>();
            while ((line = buffer.readLine()) != null) {
                System.out.println(line);
                line = line.trim();
                String[] socket_info = line.split(" ");
                String siteid = socket_info[0];
                int port = Integer.parseInt(socket_info[1]);
                phonebook.put(siteid, new int[]{index, port});
                index++;

            }
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {

        // Read system site infos and make phonebook
        readFile("known_udp.txt");

        if(args.length != 1 || !phonebook.containsKey(args[0])){
            System.out.println("ERROR: Invalid Arguments");
            System.out.println("USAGE: ./a.java <site_id>");
            System.exit(1);
        }

        // Get port number (args[0] stands for site ID)
        int port = phonebook.get(args[0])[1];

        Server server = new Server(args[0], port);
        server.setDaemon(true);
        server.start();

        Client client = new Client(args[0], port);

        Scanner sc = new Scanner(System.in);
        String command;
        while (server.getStatus()) {
            command = sc.nextLine();
            Message msg = client.parse_command(command);
            if (msg == null) continue;
            client.sendMsg(msg, args[0], port);
        }

        client.close();
    }

}