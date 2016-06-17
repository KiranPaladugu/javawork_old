package com.pack.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class NetworkTest {
	public static void main(String args[]) {
		new NetworkTest();
	}

	public NetworkTest() {
		try {
			InetAddress inetAddress = InetAddress.getByName("rome");
			System.out.println("inetAddress is : " + inetAddress);
			System.out.println("IP Address :" + inetAddress.getHostAddress());
			System.out.println("HOST:" + inetAddress.getCanonicalHostName());
			InetSocketAddress socketAddress = new InetSocketAddress("localhost", 80);
			System.out.println(socketAddress);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
