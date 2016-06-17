package com.common.log;

import java.io.IOException;
import java.util.Date;

public class Logger {
	private static Watcher logger;
	public static boolean out;
	static {
		logger = new Watcher();
	}

	public static void closeLog() {
		if (logger.writer != null)
			try {
				logger.writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private static String formatLog(String msg) {
		String str = "[ ";
		str += getTime();
		str += " ]";
		msg += "\n";
		str += msg;
		if (out) {
			System.out.println(msg);
		}
		return str;
	}

	private static String getTime() {
		Date date = new Date();
		return date.toString();
	}

	public static void log(String msg) {
		logger.log(formatLog(msg));
	}
}
