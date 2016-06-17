package com.connect;

import java.io.IOException;
import java.io.OutputStream;

public class OutputWriter implements Runnable {
	private OutputStream out ;
	private Thread thread;
	public OutputWriter(OutputStream out) {
		this.out= out;	
		thread = new Thread(this);
		thread.start();
	}
	public void run() {
		if(out!=null){
			String msg="GET";
			try {
				thread.join(100);
				out.write(msg.getBytes());
				out.flush();
				System.out.println("Flushing..");
			} catch (IOException e) {				
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
