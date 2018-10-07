package com.project1.server;


public class Server {
  int i;
  Module3 mod3;

  public Server(int i){
    this.i = i;
    mod3 = new Module3();
  }

  public void printit(){
    System.out.println("My number is " + i);
  }

  public void printModule3(){
    mod3.printit();
  }

}