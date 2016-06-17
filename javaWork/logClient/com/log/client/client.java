package com.log.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class client {
	public static void main(String[] args) {
		new client();
	}

	public client() {
		try {
			Socket server = new Socket("localhost", 7757);
			InputStream in = server.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String msg = null;
			while (true)
				if ((msg = br.readLine()) != null) {
					System.out.print(msg);
				} else {
					Thread.sleep(100);
				}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
