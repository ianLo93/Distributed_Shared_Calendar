package com.project1.client;

import java.io.*;
import java.net.*;


public class Client {

    private DatagramSocket clientSocket;
    private String siteid;
    private int port;
    private byte[] buf;

    public Client(String siteid_, int port_) {
        try {
            this.clientSocket = new DatagramSocket();
            this.siteid = siteid_;
            this.port = port_;
        }
        catch (SocketException s) {
            System.out.println(s);
        }
    }

    public Message parse_command (String command) {
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
            return new Message(cmds[1], null, null, this.siteid,
                    cmds[2], cmds[3], cmds[4], cmds[5], cmds[6].split(","));
        }
        else if (cmds[1].equals("cancel")) {
            if (cmds.length != 3) {
                System.out.println("ERROR: Invalid Meeting Cancellation");
                System.out.println("USAGE: % cancel <name>");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    cmds[2], null, null, null, null);
        }
        else if (cmds[1].equals("view")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid View Command");
                System.out.println("USAGE: % view");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        }
        else if (cmds[1].equals("myview")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid MyView Command");
                System.out.println("USAGE: % myview");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        }
        else if (cmds[1].equals("log")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid Log Command");
                System.out.println("USAGE: % log");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        }
        else if (cmds[1].equals("quit")) {
            if (cmds.length != 2) {
                System.out.println("ERROR: Invalid Exit Command");
                System.out.println("USAGE: % quit");
                return null;
            }
            return new Message(cmds[1], null, null, this.siteid,
                    null, null, null, null, null);
        }
        else {
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
}
