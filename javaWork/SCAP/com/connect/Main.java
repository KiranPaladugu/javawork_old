package com.connect;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) {
		try {
			Connector con=new Connector("172.16.70.43", 27888);
			InputReader reader = new InputReader(con.getSocketInputStream());
			@SuppressWarnings("unused")
			OutputWriter writer = new OutputWriter(con.getSocketOutputStream());
			Thread.sleep(200000);
			System.out.println("Closing Connection....");
			reader.stop();
			con.getSocket().close();
			System.out.println("Connection Closed....");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
	}
}
