package com.tcs.berReader.gui;

import com.logService.Logger;

public class GuiRunner {
	public static void main(String args[]) {
		if (args == null || args.length == 0) {
			MessageHandler.displayErrorMessage("Invalid Args..");
			System.exit(0);
		}
		boolean flag = false;
		String logPath = args[0];
		try {
			flag = Boolean.valueOf(args[1]);
		} catch (Throwable e) {
		}
		Logger.setAppender(flag);
		Logger.setPath(logPath);
		new UserInterface();
	}
}
