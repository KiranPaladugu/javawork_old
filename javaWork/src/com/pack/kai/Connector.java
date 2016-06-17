package com.pack.kai;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connector {
	Socket socket;
	private int port = 80;
	private String host = "localhost";
	private InetAddress address;

	public Connector() {
	}

	public Connector(int port, String host) {
		this.port = port;
		this.host = host;
	}

	public InetAddress getAddress() {
		return this.address;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int makeConnection() {
		try {
			socket = new Socket(host, port);
			this.address = socket.getInetAddress();
		} catch (ConnectException e) {
			System.out.println("\n\nUnable to Connect at port " + port);
			System.out.println("Possible ERRORS are..");
			System.out.println("1. Port might not be opened for Connection on HOST:" + host);
			System.out.println("2. Host is invalid on local machine.");
			System.out.println("3. Port is not valid.");
			return -1;
		} catch (UnknownHostException uhe) {
			System.out.println("\nERROR: Unbale to resolve Host adderess..");
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
		System.out.println("\n\nCONNECTION SUCCESS AT \n           -------           \nPORT : " + port);
		System.out.println("HOST : " + host);
		try {
			socket.close();
		} catch (Exception e) {
			System.out.println("\n\nEXCEPTION IN CLOSING CONNECTION..");
			e.printStackTrace();
			return -4;
		}

		return 0;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
