package com.tcs.tmp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketHandler implements Runnable{
	
	private int server =-1;
	private String name="";
	//private AtomicBoolean stop = new AtomicBoolean(false);
	
	public ServerSocketHandler(int server, String name) {
		this.server = server;
		this.name=name;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run() {
		if(server > 1){
		try {
			ServerSocket serverSocket = new ServerSocket(server);
			System.out.println("Starting Listening... :"+name+" on Port:"+server);
			Socket socket = serverSocket.accept();
			System.out.println("Got Connection for :+name"+" on Port:"+server);
			InputStream inStream = socket.getInputStream();
			int x =-1;
			while((x=inStream.read())!=-1){
				System.out.print((char)x);
			}
			inStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		
	}
	
}
