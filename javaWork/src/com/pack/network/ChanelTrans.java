package com.pack.network;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class ChanelTrans {
	private static void channelCopy2(ReadableByteChannel src, WritableByteChannel dest) throws Exception {
		ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			// Prepare the buffer to be drained
			buffer.flip();
			// Make sure that the buffer was fully drained
			while (buffer.hasRemaining()) {
				dest.write(buffer);
			}
			// Make the buffer empty, ready for filling
			buffer.clear();
		}
	}

	public static void copyChannel1(ReadableByteChannel src, WritableByteChannel dst) throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);
		while (src.read(buffer) != -1) {
			buffer.flip();
			dst.write(buffer);
			buffer.compact();
		}
		buffer.flip();
		while (buffer.hasRemaining()) {
			dst.write(buffer);
		}
	}

	public static void main(String args[]) throws Exception {
		ReadableByteChannel source = Channels.newChannel(System.in);
		WritableByteChannel destination = Channels.newChannel(System.err);
		// copyChannel1(source, destination);
		channelCopy2(source, destination);
		source.close();
		destination.close();
	}

}
