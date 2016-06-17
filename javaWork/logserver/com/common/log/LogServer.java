package com.common.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.pack.hangman.log.Logger;

public class LogServer {
	private class InitiateReader {
		private int flushCount;

		public InitiateReader() {
			run();
		}

		public void run() {
			File file = new File("TMFAgent.log");
			try {				
				FileInputStream fis= new FileInputStream(file);
				FileChannel channel = fis.getChannel();
				channel.read(buffer);				
				while (true) {
					if (connections == 1) {
						if ((channel.read(buffer) != -1)) {
							if (buffer.position() == buffer.capacity()) {
//								Thread.sleep(1000);								
								flushCount++;
								System.out.println("Buffer is full FLUSH......: "+flushCount);
								//System.out.println("BufferData:" + buffer.toString());
								buffer.clear();
								byte b[] = buffer.array();								
								Logger.log(new String(b));								
							}

						} else if (buffer.position() > 0) {
							buffer.flip();
							System.out.println("Flipping ... Buffer.");
							byte b[] = buffer.array();							
								Logger.log(new String(b));
						} else {
							Logger.log("sleeping...");
							Thread.sleep(100);
						}
					} else {
						Thread.sleep(1000);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String agrs[]) {
		new LogServer();
	}
	private ByteBuffer buffer;
	private boolean readFlag;
	private final int MAX_CONNECTIONS = 2;
	private transient int connections;
	private String message;
	private int intMessage = -1;
	public LogServer() {
		try {
			//ServerSocket server = new ServerSocket(7757);
			buffer = ByteBuffer.allocate(1024*64);
			while (true) {
				while (connections < 1) {
					System.out.println("Total Connections .. :" + connections);
					System.out.println("Waiting for Connection .....");
					//Socket client = server.accept();
					System.out.println("Got Request..");					
					connections++;
					new InitiateReader();
					System.out.println("Intiated client sending ...for client :" + connections);
				}
				if (connections > 1) {
					//Socket client = server.accept();
//					String message = "One client already Connected ..";
//					client.getOutputStream().write(message.getBytes());
//					client.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeConnection(boolean flag) {

		if (flag)
			connections--;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public int getConnections() {
		return connections;
	}
	public int getIntMessage() {
		return intMessage;
	}

	public int getMAX_CONNECTIONS() {
		return MAX_CONNECTIONS;
	}

	public String getMessage() {
		return message;
	}

	public boolean isReadFlag() {
		return readFlag;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}
}
