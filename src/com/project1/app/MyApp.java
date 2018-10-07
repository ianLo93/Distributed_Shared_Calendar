package com.project1.app;

import java.util.*;
import com.project1.client.Client;
import com.project1.server.Server;

public class MyApp {

  public static void main(String args[]){

    if(args.length < 1){
        System.out.println("ERROR: Please include 1 argument");
        System.exit(1);
    }
    Client a = new Client(args[0]);
    Server b = new Server(101);

    a.printit();
    b.printit();
    b.printModule3();

    ArrayList<String> lis = new ArrayList<String>();
    lis.add("Testing");
    lis.add("importing");
    lis.add("a");
    lis.add("foreign");
    lis.add("module.");

    for(String item : lis){
      System.out.println(item);
    }

  }

}