package com.project1.client;
import java.io.*;
import java.net.*;


public class Client {

    private DatagramSocket clientSocket;
    private String siteid;
    private InetAddress addr;
    private byte[] buf;

    public Client(String hostname) {
        try {
            this.clientSocket = new DatagramSocket();
            this.addr = InetAddress.getByName("localhost");
            this.siteid = hostname;
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
