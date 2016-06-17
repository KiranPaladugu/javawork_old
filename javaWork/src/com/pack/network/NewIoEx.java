package com.pack.network;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NewIoEx {
	public static void main(String args[]) {
		new NewIoEx();
	}

	public NewIoEx() {
		try {
			int flushCount = 0;
			int capacity = 524288;
			File file = new File("TMFAgent.log");
			String mode = "r";
			InetSocketAddress socketAddress = new InetSocketAddress(9856);
			System.out.println("is unresolved:" + socketAddress.isUnresolved());
			// SocketChannel sc=SocketChannel.open(socketAddress);
			// DatagramChannel dc= DatagramChannel.open();
			RandomAccessFile raf = new RandomAccessFile(file, mode);
			FileChannel fcChannel = raf.getChannel();
			System.out.println("is file channel is opern:" + fcChannel.isOpen());
			System.out.println("File size : " + file.length());
			float l = file.length();
			float ca = capacity;
			float time = (l / ca);
			System.out.print("Estimated Time :");
			System.out.format("%.3f", time);
			System.out.println(" seconds");

			int c = -1;
			ByteBuffer buffer = ByteBuffer.allocate(capacity);
			while ((c = fcChannel.read(buffer)) != -1) {
				if (buffer.position() == buffer.capacity()) {
					Thread.sleep(1000);
					// System.out.println("Buffer is full FLUSH......");
					flushCount++;
					System.out.println((time - flushCount) + " sec Time remaining...");
					// System.out.println("BufferData:"+buffer.toString());
					buffer.clear();
					// byte b[]=buffer.array();
					// for(int i=0;i<b.length;i++)
					// {
					// System.out.print((char)b[i]);
					// }
				}

			}
			buffer.flip();
			/*
			 * byte b[]=buffer.array(); for(int i=0;i<buffer.limit();i++) {
			 * System.out.print((char)b[i]); }
			 */
			System.out.println("Buffer:" + buffer.toString());
			System.out.println("Total flushes : " + flushCount);
			System.out.println("Download Speed:" + (capacity / (1024 * 8)) + " KBps");
			System.out.println("File Size:" + ((flushCount * capacity) + buffer.limit()) + " bytes");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
