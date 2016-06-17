package com.tcs.tmp;

import com.tcs.berReader.gui.MessageHandler;

public class CommandLineParser {
	String [] args=null;
	public CommandLineParser(String[] args) {
		this.args = args;
		if(args == null || args.length==0){
			MessageHandler.displayErrorMessage("Invalid Arguments.");
		}
	}
}
