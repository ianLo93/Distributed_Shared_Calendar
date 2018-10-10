package com.project1.server;


import com.project1.client.Client;
import com.project1.client.Message;

import java.io.*;
import java.net.*;
//import java.util.*;

public class Server extends Thread {

    private DatagramSocket serverSocket;
    private Site mySite;
    private byte[] buf;
    private String siteid;
    private int port;

    public Server(String siteid_, int port_) {
        try {
            this.serverSocket = new DatagramSocket(port);
            this.siteid = siteid_;
            System.out.println(siteid);
            this.port = port_;
            this.mySite = new Site(siteid, port);
        } catch (SocketException s) {
            System.out.println(s);
        }
    }

    private void exe_cmd(Message recvMsg) {
        // Get message command
        String cmd = recvMsg.getMsg();

        if ( recvMsg.getSender().equals(siteid) ) {
            String day, start, end;
            String[] participants;
            if (cmd.equals("schedule")) {
                System.out.println("You get here");
                participants = recvMsg.getMeeting().getParticipants();
                day = recvMsg.getMeeting().getDay();
                start = recvMsg.getMeeting().getStartTime();
                end = recvMsg.getMeeting().getEndTime();
                if ( mySite.hasConflict(day, start, end, participants) ) {
                    System.out.println(
                            "Unable to schedule meeting " + recvMsg.getMeeting().getName());
                } else {
                    // TODO schedule meeting
                    // TODO update T, schedule, log
                    // TODO make NP, insert T and NP to message
                    // TODO send message to participants
                    Client client = new Client(siteid, port);
                }
            } else if (cmd.equals("cancel")) {
                System.out.println("You get here");
                // TODO check meeting
                // TODO cancel meeting
                // TODO update T, schedule, log
                // TODO make NP, insert T and NP to message
                // TODO send messages to participants
            } else if (cmd.equals("view")) {
                mySite.view(); // Call view() to print calendar
            } else if (cmd.equals("myview")) {
                mySite.myView(); // Call myView() to print my schedule
            } else if (cmd.equals("log")) {
                mySite.viewLog(); // Call viewLog() to print all logs in my site
            } else {
                System.out.println("ERROR: This should not happen");
            }
        } else {
            // TODO: update my site according to NP
            // TODO: handle conflicts
        }
    }

    @Override
    public void run() {
        boolean running = true;

        while (running) {
            try {
                // Create datagram packet holder and send received msg to buf
                buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                System.out.println(packet.getLength());
                serverSocket.receive(packet);
                System.out.println(packet.getLength());

                // Get byte data and send to object stream
                byte[] data = packet.getData();
                ByteArrayInputStream byIn = new ByteArrayInputStream(data);
                ObjectInputStream objIn = new ObjectInputStream(byIn);

                // Get the object message
                Message recvMsg;
                try {
                    recvMsg = (Message) objIn.readObject();
                } catch (ClassNotFoundException c) {
                    System.out.println(c);
                    continue;
                }

                System.out.println(recvMsg.getSender());

                if (recvMsg.getMsg().equals("quit")) {
                    running = false;
                    mySite.save_state();
                    continue;
                }

                // Execute commands
                exe_cmd(recvMsg);

                buf = null;

            } catch (IOException i) {
                mySite.save_state();
                System.out.println(i);
                break;
            }
        }
    }

}