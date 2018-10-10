package com.project1.server;


import java.io.*;
import java.net.*;

public class Server extends Thread {
  private DatagramSocket serverSocket;
  private Site mySite;
  private byte[] buf;
  private boolean running;
  private String siteid;
  private int port;

  public Server(String hostname_, int port_){
    try {
        this.serverSocket = new DatagramSocket(port);
        this.siteid = hostname_;
        this.port = port_;
        this.mySite = new Site(siteid, port);
    }
    catch (SocketException s) {
        System.out.println(s);
    }
  }

  @Override
  public void run() {
      running = true;

      while (running) {
          try {
              // Create datagram packet holder and send received msg to buf
              buf = new byte[1024];
              DatagramPacket packet = new DatagramPacket(buf, buf.length);
              serverSocket.receive(packet);

              // Get packet sender address and port
              InetAddress addr = packet.getAddress();
              int port = packet.getPort();

              // Make the same text to be a new datagram packet
              packet = new DatagramPacket(buf, buf.length, addr, port);
              String msg = new String(packet.getData());
              msg = msg.trim();

              // execute command
              String [] cmds = msg.split(" ");

              if (cmds.length < 2) {
                  System.out.println("Invalid command");
                  continue;
              }
              else if (cmds[1].equals("schedule")){
                  if (cmds.length != 7 ) System.out.println("Invalid meeting schedule command");
                  else if (mySite.checkAvailabilityOfAll(cmds[3], cmds[4], cmds[5], cmds[6])){
                      // send invitation to all sites.

                  }
                  else {
                      System.out.println("Unable to schedule meeting "+ cmds[2]);
                  }

              }
              else if (cmds[1].equals("cancel")){
                  // check if meeting exists

                  // cancel myself

                  // send cancel to all participants
              }
              else if (cmds[1].equals("view")){
                  mySite.view();

              }
              else if (cmds[1].equals("myview")){
                  mySite.myView();

              }
              else if (cmds[1].equals("log")){
                  mySite.viewLog();
              }
              else {
                  System.out.println("Invalid command");
              }

              // End instruction
              if (msg.equals("end")) {
                  running = false;
                  buf = null;
                  continue;
              }
              System.out.println(msg+"a");
              // If not end, send the text back
              serverSocket.send(packet);
              buf = null;
          }
          catch (IOException i) {
              System.out.println(i);
          }
      }
  }

}