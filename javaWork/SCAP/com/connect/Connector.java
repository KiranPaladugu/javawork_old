package com.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connector {
	Socket socket;
	String host;
	int port;
	public Connector(String host,int port) throws UnknownHostException, IOException {
		this.host=host;
		this.port = port;
		socket =new Socket(host, port);
	}
	public Socket getSocket(){
		return socket;
	}
	public InputStream getSocketInputStream() throws IOException{
		if((socket != null)||(!socket.isInputShutdown())){
			return socket.getInputStream();
		} else {
			return null;
		}
	}
	public OutputStream getSocketOutputStream() throws IOException{
		if((socket!=null)||(!socket.isOutputShutdown())){
			return socket.getOutputStream();
		} else {
			return null;
		}
	}
}
