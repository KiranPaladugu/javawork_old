package com.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputReader implements Runnable {
	private InputStream input;
	private BufferedReader reader;
	private Thread thread;
	private int count;
	public InputReader(InputStream in) {
		input=in;
		reader = new BufferedReader(new InputStreamReader(input));
		thread = new Thread(this);
		thread.start();
	}
	private boolean stop;
	public void run(){
		if(reader != null ){
			int c=-1;
			while(!stop){				
				try {
					if((c=reader.read()) !=-1){
						System.out.print((char)c);
					} else {
						thread.join(100);
						if(count <=40){
							System.out.print("*");
						}else{
							System.out.println("*");
							count=0;
						}
						count++;
					}
				} catch (IOException e) {					
					e.printStackTrace();
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
			}
		}
	}
	public void stop(){
		stop = true;
	}
}
