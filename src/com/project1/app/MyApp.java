package com.project1.app;

import java.util.*;
import com.project1.client.Module1;
import com.project1.server.Module2;

public class MyApp {

  public static void main(String args[]){

    if(args.length < 1){
        System.out.println("ERROR: Please include 1 argument");
        System.exit(1);
    }
    Module1 a = new Module1(args[0]);
    Module2 b = new Module2(101);

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