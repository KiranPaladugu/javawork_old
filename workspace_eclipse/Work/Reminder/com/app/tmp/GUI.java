package com.app.tmp;

import com.app.reminder.gui.MessageHandler;

public class GUI {
	private static int instances;
	public static void start(String args[]){
		if(instances >1){
			
		}else{
			MessageHandler.displayErrorMessage("Unable to Start Application...", " is already Running...");
		}
	}
	
	
}
