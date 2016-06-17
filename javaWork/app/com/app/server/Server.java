package com.app.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.app.logservice.Logger;

public class Server {
	private ServerSocket server = null;
	private boolean stop =false;
	public Server() {
		init();
	}
	private void init() {
		try {
			server = new ServerSocket(800);
			Logger.log("Initalized Server...");
			Logger.log("Going to Wait for ever for connection..");
			listenForEver();
			
		} catch (IOException e) {						
			Logger.log(e.getLocalizedMessage());
			Logger.log("Unable to start Server ...  Now Exitng.");
			System.out.println(e);
			System.err.println("Unable to start Server ...  Now Exitng.");
			System.exit(0);
		}
	}
	private void listenForEver() {
		while(!stop){
			try {
				new RequestHandler(server.accept());
			} catch (IOException e) {
				
			}
		}
	}
}
