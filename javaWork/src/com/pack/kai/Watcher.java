package com.pack.kai;

import java.io.File;

public class Watcher {
	private static Watcher watcher = new Watcher();

	public static Watcher getWatcher() {
		return (watcher);
	}

	private Watcher() {
		this.watchInit();
	}

	public void headLog(String header) {
		System.out.println();
		System.out.println("***************************");
		System.out.println(header);
		System.out.println("***************************");
		System.out.println();
	}

	public void log(String msg) {
		System.out.println(msg);
	}

	public void log(String msg, char style) {
		System.out.println(style + style + style + "  " + msg + style + style + style);
	}

	public void mLog(String msg) {
		System.out.println("****   " + msg + "    *****");
	}

	private void watchInit() {
		String path = new File("").getAbsolutePath();

		System.out.println(path);
		System.out.println("***************************");
		System.out.println("Initializing Watcher ....");
		System.out.println("***************************");
		System.out.println();
		System.out.println("*** No Config file Found ***");
		System.out.println("*** No Config Specified  ***");
		System.out.println("Setting stdOut,stdErr as System.out ...");
		System.out.println("****  SUCCESS  *****");
		System.out.println();
	}
}
