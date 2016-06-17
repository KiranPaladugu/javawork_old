package com.pack.network;

import java.nio.CharBuffer;

public class BufferFillDrain {
	private static int index = 0;
	private static String strings[] = { "a random Strings", "another String", "one string", "two strings", "some string" };

	public static void drainBuffer(CharBuffer buffer) {
		while (buffer.hasRemaining()) {
			System.out.println("\ndrainBuffer():Buffer position before get: " + buffer.position());
			System.out.println("char in buffer:" + buffer.get());
			System.out.println("drainBuffer():Buffer position after get: " + buffer.position());
		}
	}

	public static boolean fillBuffer(CharBuffer buffer) {
		if (index >= strings.length) {
			return false;
		}
		String string = strings[index++];
		for (int i = 0; i < string.length(); i++) {
			System.out.println("\nfillBuffer();Buffer position before put:" + buffer.position());
			buffer.put(string.charAt(i));
			System.out.println("Putting char:" + string.charAt(i));
			System.out.println("fillBuffer();Buffer position after put:" + buffer.position());
		}
		return true;
	}

	public static void main(String args[]) throws Exception {
		CharBuffer buffer = CharBuffer.allocate(100);
		while (fillBuffer(buffer)) {
			System.out.println("\nmain():Buffer position before calling flip:" + buffer.position());
			System.out.println("main():Buffer limit before calling flip:" + buffer.limit());
			System.out.println("******************************** FLIPPING *****************************");
			buffer.flip();
			System.out.println("main():Buffer position after calling flip:" + buffer.position());
			System.out.println("main():Buffer limit after calling flip:" + buffer.limit());
			drainBuffer(buffer);
			buffer.clear();
		}
	}
}
