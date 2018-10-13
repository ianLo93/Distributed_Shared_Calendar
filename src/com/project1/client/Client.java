package com.project1.client;

import com.project1.app.Calendar;
import com.project1.server.Site;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class Client {

    private DatagramSocket clientSocket;
    private String siteid;
    private byte[] buf;

    public Client(String siteid_, int port_) {
        try {
            this.clientSocket = new DatagramSocket();
            this.siteid = siteid_;
        } catch (SocketException s) {
            System.out.println(s);
        }
    }

    public Message parse_command(String command) {
        // Parse command
        String[] cmds = command.split(" ");
        // Error checking
        if (cmds.length < 2 || !cmds[0].equals("%")) {
            System.out.println("ERROR: Invalid Command");
            System.out.println("USAGE: % <command> [<meeting_info>]");
            return null;
        }
        // Implement command "schedule", "cancel", "view", "myview", "log"
        if (cmds[1].equals("schedule")) {
            if (cmds.length != 7) {
                System.out.println("ERROR: Invalid Meeting Schedule");
                System.out.println("USAGE: % schedule <name> <day> <start_time> " +
                        "<end_time> <participants>");
                return null;
            }
            if (cmds[3] != "10/14/2018" && cmds[3] != "10/15/2018" && cmds[3] != "10/16/2018" &&
                    cmds[3] != "10/17/2018" && cmds[3] != "10/18/2018" && cmds[3] != "10/19/2018" &&
                    cmds[3] != "10/20/2018") {
                System.out.println("DAY SCHEDULE ERROR: <day> Format Error");
                return null;
            }
            String[] participants = valid_users(cmds[6].split(","));
            if (participants.length == 0) {
                System.out.println("SCHEDULE ERROR: No Valid User Provided");
                return null;
            }
            Site tmp = new Site(siteid, 5000);
            int s = tmp.parse_time(cmds[4]);
            int e = tmp.parse_time(cmds[5]);
            if (s < 0 || s > 48 || e < 0 || e > 48 || e < s) {
                System.out.println("TIME SCHEDULE ERROR: <start_time> or <end_time> Format Error");
                return null;
            }

            return new Message(cmds[1], null, null, this.siteid,
                    cmds[2], cmds[3], cmds[4], cmds[5], participants);
        } else if (cmds[1].equals("cancel")) {
            if (cmds.length != 3) {
                System.out.println("ERROR: Invalid Meeting Cancellation");
                System.out.println("USAGE: % cancel <name>");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    cmds[2], null, null, null, null);
        } else if (cmds[1].equals("view")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid View Command");
                System.out.println("USAGE: % view");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        } else if (cmds[1].equals("myview")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid MyView Command");
                System.out.println("USAGE: % myview");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        } else if (cmds[1].equals("log")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid Log Command");
                System.out.println("USAGE: % log");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        } else if (cmds[1].equals("quit")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid Exit Command");
                System.out.println("USAGE: % quit");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        } else if (cmds[1].equals("init")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid Initialization Command");
                System.out.println("USAGE: % init");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        } else {
            System.out.println("ERROR: Invalid Command");
            System.out.println("USAGE: % <command> [<meeting_info>]");
            return null;
        }
    }

    public void sendMsg(Message msg, String siteid_, int port_) {
        try {
            // Write object on stream buffer
            ByteArrayOutputStream byOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(byOut);
            objOut.writeObject(msg);
            // Close output stream
            objOut.close();
            // Output to buffer
            buf = byOut.toByteArray();

            // Put obj into datagram packet and mark the address and port
            InetAddress addr = InetAddress.getByName(siteid_);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port_);

            // Send packets
            clientSocket.send(packet);
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public void close() {
        clientSocket.close();
    }

    private String[] valid_users(String[] participants) {
        ArrayList<String> valid_part = new ArrayList<>();
        for (String p : participants) {
            if (Calendar.phonebook.containsKey(p)) valid_part.add(p);
        }
        String[] vp = new String[valid_part.size()];
        for (int i=0; i<valid_part.size(); i++) vp[i] = valid_part.get(i);
        return vp;
    }
}
