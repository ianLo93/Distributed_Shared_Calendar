package com.project1.server;


import java.io.IOException;
import java.net.*;

public class Server extends Thread {
  private DatagramSocket serverSocket;
  private byte[] buf = new byte[1024];
  boolean running;

  public Server(int port){
    try {
        serverSocket = new DatagramSocket(port);
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
              // Create new datagram packet holder and send received msg to buf
              DatagramPacket packet = new DatagramPacket(buf, buf.length);
              serverSocket.receive(packet);

              // Get packet sender address and port
              InetAddress addr = packet.getAddress();
              int port = packet.getPort();

              // Make the same text to be a new datagram packet
              packet = new DatagramPacket(buf, buf.length, addr, port);
              String msg = new String(packet.getData(), 0, packet.getLength());

              // End instruction
              if (msg == "end") {
                  running = false;
                  continue;
              }

              // If not end, send the text back
              serverSocket.send(packet);
          }
          catch (IOException i) {
              System.out.println(i);
          }
      }
  }

}