package com.pack.hangman.serv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.pack.hangman.log.Logger;

public class NewClientHandler implements Runnable {
	public static int activeConnections = 0;
	public static int getActive() {
		return activeConnections;
	}
	private Socket client;
	private Thread t;
	private InputStream input;

	private OutputStream out;

	public NewClientHandler(Socket client) {
		this.client = client;
		t = new Thread(this);
		t.start();
	}

	public Socket getClient() {
		return this.client;
	}

	public void run() {
		if (client != null) {
			if (!client.isClosed()) {
				activeConnections++;
				try {
					input = client.getInputStream();
					InputStreamReader reader = new InputStreamReader(input);
					char ch[] = new char[input.available()];
					reader.read(ch);
					String str = new String(ch);
					Logger.log(str);
					if (str.contains("getDataUpdate")) {
						Logger.log("Yahoo.......");
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Server.cc--;
		}
	}
}
