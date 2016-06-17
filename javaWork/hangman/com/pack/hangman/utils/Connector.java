package com.pack.hangman.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.pack.hangman.data.Data;

public class Connector {
	private int port;
	private String host;
	private Socket socket;
	private InputStream input;
	private OutputStream output;
	public static final int DATA_UPDATE = 1;
	public static final int PROGRAM_UPDATE = 2;

	public Connector() {
		host = "localhost";
		port = 7337;
		socket = getConnection(host, port);
	}

	public Connector(String host, int port) {
		this.host = host;
		this.port = port;
		socket = getConnection(host, port);
	}

	public boolean closeConnection(Socket connection) {
		if (connection != null) {
			if (!connection.isClosed()) {
				try {
					connection.close();
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	public Socket getConnection(String host, int port) {
		this.host = host;
		this.port = port;
		Socket socket = null;
		try {
			socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to Update Server.", "UPDATE", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to Update Server.", "ERROR!!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return socket;
	}

	public InputStream getInputStream() {
		if (socket != null) {
			if (!socket.isClosed()) {
				try {
					input = socket.getInputStream();
					return input;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return input;
				}
			}
		}
		return input;
	}

	public OutputStream getOutputStream() {
		if (socket != null) {
			if (!socket.isClosed()) {
				try {
					output = socket.getOutputStream();
					return output;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return output;
				}
			}
		}
		return output;
	}

	public Socket getSocket() {
		return socket;
	}

	public Data getUpdate(int type) {
		Data data = null;
		String msg = "";
		switch (type) {
		case Connector.DATA_UPDATE:
			msg = "getDataUpdate";
			break;
		case Connector.PROGRAM_UPDATE:
			msg = "getProgramUpdate";
			break;
		default:
			msg = "getDataUpdate";
		}
		if (output != null) {

			byte b[] = msg.getBytes();
			try {
				output.write(b);
				output.flush();
				if (input != null) {
					ObjectInputStream ois = new ObjectInputStream(input);
					try {
						data = (Data) ois.readObject();
						return data;
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}
}
