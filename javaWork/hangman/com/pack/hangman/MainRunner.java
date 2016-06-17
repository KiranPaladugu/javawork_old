package com.pack.hangman;

import com.pack.hangman.data.DataManager;
import com.pack.hangman.err.HangedException;
import com.pack.hangman.log.Logger;

public class MainRunner {
	public static void main(String args[]) {
		Logger logger = new Logger();
		logger.log("hello");
		try {
			new DataManager();
		} catch (HangedException e) {
			// JOptionPane.showMessageDialog(null,"\nSORRY!!! \n EXITING ..",null,JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}
