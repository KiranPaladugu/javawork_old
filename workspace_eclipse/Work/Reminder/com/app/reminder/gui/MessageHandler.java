package com.app.reminder.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageHandler {
	
	public static void displayInformationMessage(String title,String message){
		JOptionPane.showMessageDialog(getFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void displayWarningMessage(String title,String message){
		JOptionPane.showMessageDialog(getFrame(), message, title, JOptionPane.WARNING_MESSAGE);
	}
	public static void displayErrorMessage(String title,String message){
		JOptionPane.showMessageDialog(getFrame(), message, title, JOptionPane.ERROR_MESSAGE);
	}

	private static Component getFrame() {
		return null;
	}
}
