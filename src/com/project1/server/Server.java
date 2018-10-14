package com.project1.server;

import com.project1.app.Calendar;
import com.project1.client.Message;

import java.io.*;
import java.net.*;

public class Server extends Thread {

    private DatagramSocket serverSocket;
    private Site mySite;
    private boolean running;
    private String siteid;
    private int port;

    public Server(String siteid_, int port_) {
        try {
            this.serverSocket = new DatagramSocket(port_);
            this.siteid = siteid_;
            this.port = port_;
            this.mySite = new Site(siteid, port);
        } catch (SocketException s) {
            System.out.println(s);
        }
    }

    public boolean getStatus() { return running; }

    private void exe_cmd(Message recvMsg) {
        // Get message command
        String cmd = recvMsg.getMsg();

        if ( recvMsg.getSender().equals(siteid) ) {
            String day, start, end;
            String[] participants;
            if (cmd.equals("schedule")) {
                // Get meeting time info
                participants = recvMsg.getMeeting().getParticipants();
                day = recvMsg.getMeeting().getDay();
                start = recvMsg.getMeeting().getStartTime();
                end = recvMsg.getMeeting().getEndTime();
                // Time conflicts
                if (mySite.hasConflict(day, start, end, participants)) {
                    System.out.println(
                            "Unable to schedule meeting " + recvMsg.getMeeting().getName());
                } else {
                    // Update T, schedule, log
                    mySite.addMeeting(recvMsg.getMeeting(), false);
                    // Send to participants
                    mySite.sendMessage(recvMsg);
                }
            } else if (cmd.equals("cancel")) {
                Meeting m = mySite.getMeetingByName(recvMsg.getMeeting().getName());
                // Remove meeting if it exists
                if (m != null) {
                    recvMsg.setMeeting(m);
                    // Update T, schedule, log
                    mySite.rmMeeting(m, false);
                    // Send to participants
                    mySite.sendMessage(recvMsg);
                }
            } else if (cmd.equals("view")) {
                mySite.view(); // Call view() to print calendar
            } else if (cmd.equals("myview")) {
                mySite.myView(); // Call myView() to print my schedule
            } else if (cmd.equals("log")) {
                mySite.viewLog(); // Call viewLog() to print all logs in my site
            } else if (cmd.equals("init")){
                mySite.init(siteid, port);
                mySite.save_state();
            } else {
                System.out.println("ERROR: This should not happen");
            }
        } else {
            // Update my site according to NP
            mySite.update(recvMsg);
            // Handle conflicts
            mySite.handle_conflict();
        }
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                // Create datagram packet holder and send received msg to buf
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                serverSocket.receive(packet);

                // Get byte data and send to object stream
                byte[] data = packet.getData();
                ByteArrayInputStream byIn = new ByteArrayInputStream(data);
                ObjectInputStream objIn = new ObjectInputStream(byIn);

                // Get the message object
                Message recvMsg;
                try {
                    recvMsg = (Message) objIn.readObject();
                } catch (ClassNotFoundException c) {
                    System.out.println(c);
                    continue;
                }
                // Close input stream
                objIn.close();

                // Quit when receive command "% quit"
                if (recvMsg.getMsg().equals("quit")) running = false;
                // Else execute commands
                else exe_cmd(recvMsg);

                if (recvMsg.getSender().equals(siteid)) Calendar.mutex.release();
                // Print MY SITE INFO for debug propose
//                System.out.println(mySite);

            } catch (IOException i) {
                System.out.println(i);
                running = false;
                break;
            } finally {
                mySite.save_state();
            }
        }
    }

}
