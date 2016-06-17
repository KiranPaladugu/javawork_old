package com.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import com.resources.PropertyFinder;

public class Connection {
	private int port;
	private SocketChannel socketChannel;
	private String host;
	public Connection(int port) throws IOException {
		this.port = port;
		makeConnection();
	}	
	private void makeConnection() throws IOException{
		host = PropertyFinder.getProperty("connection.server");
		SocketAddress remote = new InetSocketAddress(host,80);
		SocketChannel channel = SocketChannel.open(remote);		
		channel.connect(remote);
	}
	public SocketChannel getSocketChannel(){
		return socketChannel;
	}
	public int getPort(){
		return port;
	}
	public static void main(String[] args) throws IOException{
		 new Connection(80);
		
	}
}
