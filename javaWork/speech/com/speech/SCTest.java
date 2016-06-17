package com.speech;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class SCTest {
	SocketChannel channel;
	int count;
	public SCTest() {
	}
	public String readLine(SocketChannel channel){
		StringBuffer str = new StringBuffer();		
		ByteBuffer buffer = ByteBuffer.allocate(23);
		try {
			channel.read(buffer);
			buffer.flip();
			byte b[]=buffer.array();
			for(int i=0;i<b.length;i++)
				str.append((char)b[i]);			
			count++;
			System.out.println(str.toString()+" - : "+count);			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return str.toString();
	}
	public static void main(String args[]){
		SCTest c = new SCTest();
		try {
			SocketChannel channel = SocketChannel.open();
			channel.connect(new InetSocketAddress("localhost", 8788));			
			while(true){
				if(channel.isConnected()){
					c.readLine(channel);
				}
			}
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
