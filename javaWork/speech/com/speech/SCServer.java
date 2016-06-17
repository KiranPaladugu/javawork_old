package com.speech;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SCServer {
	SocketChannel serverChannel;
	public SCServer() {
	}
	public void write(String msg,SocketChannel serverChannel){
		byte b[]=msg.getBytes();
		ByteBuffer src= ByteBuffer.wrap(b);		
		try {
			serverChannel.write(src);		
		} catch (IOException e) {								
			try {
				System.out.println("Client disconnected un expectedly... now terminating Connection.");
				serverChannel.close();
			} catch (IOException e1) {				
				e1.printStackTrace();
			}
		}
	}
	public static void main(String args[]){
		SCServer scs=new SCServer();
		try {
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			serverChannel.socket().bind(new InetSocketAddress(8788));
			System.out.println("Watiting for connection...");
			SocketChannel server = serverChannel.accept();
			System.out.println("Got Connection....");
			while(true){
				if(!server.isOpen()){
					break;
				} else {
					scs.write("Hello there how are u ?", server);
				}
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		System.out.println("Exiting main...");
	}
	
}
