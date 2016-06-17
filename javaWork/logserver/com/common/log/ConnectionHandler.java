package com.common.log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
	private Socket client;
	private boolean isByteChannel;
	private boolean readFlag;
	private LogServer logServer;
	OutputStream out;

	public ConnectionHandler(Socket client, boolean isByteChannel, LogServer logServer) {
		this.client = client;
		this.isByteChannel = isByteChannel;
		this.logServer = logServer;
		try {
			out = client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void bufferChannel() {
	}

	public void byteChannel() {
		if (client.isConnected()) {
			try {
				out.write(logServer.getMessage().getBytes());
				readFlag = !readFlag;
			} catch (Exception e) {
				e.printStackTrace();
				logServer.closeConnection(true);
			}
		}
	}

	public Socket getClientSocket() {
		return client;
	}

	public boolean isupdate() {
		return readFlag;
	}

	public boolean isUpdated() {
		boolean flag = false;
		if (readFlag && !logServer.isReadFlag()) {
			flag = true;
		} else if (!readFlag && logServer.isReadFlag()) {
			flag = true;
		}
		return flag;
	}

	public void run() {
		if (isByteChannel) {
			byteChannel();
		} else {
			bufferChannel();
		}
	}
}
