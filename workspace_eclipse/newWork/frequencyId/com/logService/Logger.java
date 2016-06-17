package com.logService;

import java.io.IOException;
import java.util.Date;

public class Logger {
	private static Watcher logger;
	public static boolean out;
	static {
		logger = new Watcher();		
	}

	public static void closeLog() {
		if (logger.getWriter() != null)
			try {
				log("Closing Log... Done!");
				logger.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void setPath(String path){
		logger.setPath(path);
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
