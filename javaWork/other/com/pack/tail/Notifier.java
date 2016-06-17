package com.pack.tail;

public class Notifier {
	public static String msg = null;

	public static String notify(String message) {
		println(message);
		msg = message;
		return msg;
	}

	private static void print(String message) {
		System.out.print(message);
	}

	private static void println(String message) {
		System.out.println(message);
	}
}
