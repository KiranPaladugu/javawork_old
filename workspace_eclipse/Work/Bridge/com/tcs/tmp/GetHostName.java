package com.tcs.tmp;

import java.net.*;

public class GetHostName {
	public static void main(String[] args) {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			//byte[] ipAddr = addr.getAddress();
			String hostname = addr.getHostName();
			System.out.println("Your Host Name is : " + hostname);
		} catch (UnknownHostException e) {
		}

	}
}