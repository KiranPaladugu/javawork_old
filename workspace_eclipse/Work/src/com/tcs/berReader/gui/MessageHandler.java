package com.tcs.berReader.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.framework.reg.Register;

public class MessageHandler {

	private static JFrame getParent(){
		UserInterface frame = (UserInterface) Register.getObject(UserInterface.class);
		if(frame !=null)
		return frame.getFrame();
		return null;
		
	}
	public static void displayErrorMessage(String string) {
		JOptionPane.showMessageDialog(getParent(), string, "ERROR!!", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void displayInfoMessage(String string){
		JOptionPane.showMessageDialog(getParent(), string, "Info!!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static String dispalyInputMessage(String message){
		String str = "";
		str = JOptionPane.showInputDialog(getParent(), message);
		if(str == null || str.equals("")){			
			str = null;
		}
		return str;
	}

}
