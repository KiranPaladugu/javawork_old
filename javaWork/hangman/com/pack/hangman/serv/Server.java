package com.pack.hangman.serv;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	private ServerSocket server;
	private int port = 7337;
	public static int cc = 0;
	Thread t;

	public Server() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (cc <= 100) {
			if (cc < 0)
				cc = (cc * (-1));
			try {
				new NewClientHandler(server.accept());
				cc++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
