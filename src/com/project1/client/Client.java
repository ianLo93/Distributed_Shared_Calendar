package com.project1.client;
import java.util.*;
import com.project1.meeting.Meeting;


import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public String getSite_id() {
        return site_id;
    }



    String site_id;

    ArrayList<Meeting> events;

    ArrayList<String> T;

    ArrayList<Integer> avaible;




    public Client(String id, Integer n){
        this.site_id = id;
        this.avaible = new ArrayList<Integer>(Collections.nCopies(60, 0));
        char[] chars = new char[n];
        Arrays.fill(chars, '0');
        String s = new String(chars);
        this.T = new ArrayList<String>(Collections(n), s);
    }




    public void printit(){ System.out.println("My id is " + site_id); }

    private DatagramSocket clientSocket;
    private InetAddress addr;
    private byte[] buf;

    public Client() {
        try {
            clientSocket = new DatagramSocket();
            addr = InetAddress.getByName("localhost");
        }
        catch (SocketException s) {
            System.out.println(s);
        }
        catch (UnknownHostException u) {
            System.out.println(u);
        }
    }

    public String sendMsg(String msg, int port) {
        try {
            // Convert msg to bytes array
            buf = msg.getBytes();
            // Put text into datagram packet and mark the address and port
            DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port);
            // Send packets
            clientSocket.send(packet);
            // Empty packet
            packet = new DatagramPacket(buf, buf.length);
            clientSocket.receive(packet);
            // Convert datagram packet into string
            String recvMsg = new String(packet.getData(), 0, packet.getLength());
            return recvMsg;
        }
        catch (IOException i) {
            System.out.println(i);
            return "";
        }
    }

    public void close() {
        clientSocket.close();
    }
}
