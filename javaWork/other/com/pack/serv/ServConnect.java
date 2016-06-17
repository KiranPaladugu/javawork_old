package com.pack.serv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServConnect {
	int port = 8888;
	ServerSocket server = null;
	Socket client = null;

	public ServConnect() {
		try {
			while (true) {
				server = new ServerSocket(port);
				client = server.accept();
				new ClientConnect(client);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
