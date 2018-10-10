package com.project1.server;


import com.project1.client.Client;

import java.io.*;
import java.net.*;
import java.util.Map;

public class Server extends Thread {

    private DatagramSocket serverSocket;
    private ByteArrayInputStream byIn;
    private ObjectInputStream objIn;
    private Site mySite;
    private byte[] buf;
    private boolean running;
    private String siteid;
    private int port;

    public Server(String hostname_, int port_) {
        try {
            this.serverSocket = new DatagramSocket(port);
            this.siteid = hostname_;
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
        running = true;

        while (running) {
            try {
                // Create datagram packet holder and send received msg to buf
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                serverSocket.receive(packet);

                // Get packet sender address and port
                InetAddress addr = packet.getAddress();
                int port = packet.getPort();

                // Get byte data and send to object stream
                buf = packet.getData();
                byIn = new ByteArrayInputStream(buf);
                objIn = new ObjectInputStream(byIn);

                // Get the object message
                Message recvMsg;
                try {
                    recvMsg = (Message) objIn.readObject();
                } catch (ClassNotFoundException c) {
                    System.out.println(c);
                    continue;
                }

                // Execute commands
                exe_cmd(recvMsg);

            } catch (IOException i) {
                mySite.save_state();
                System.out.println(i);
                break;
            }
        }
    }

}