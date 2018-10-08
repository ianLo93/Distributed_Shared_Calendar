package com.project1.app;

import java.util.*;
import com.project1.client.Client;
import com.project1.server.Server;

public class MyApp {

  public static void main(String args[]){

//    if(args.length < 1){
//        System.out.println("ERROR: Please include 1 argument");
//        System.exit(1);
//    }

    Server server = new Server(5000);
    server.setDaemon(true);
    server.start();

    Client client = new Client();

    Scanner sc = new Scanner(System.in);
    String line = "";
    while (!line.equals("end")) {
      line = sc.nextLine();
      String msg = client.sendMsg(line, 5000);
      System.out.println(msg);
    }

    client.close();

  }

}