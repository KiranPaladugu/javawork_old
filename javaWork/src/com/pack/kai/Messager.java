package com.pack.kai;

import javax.swing.JOptionPane;

public class Messager {

	public static int showMessage(int code, int port, String host, Connector con) {
		switch (code) {
		case 0:
			JOptionPane.showMessageDialog(null, "Connection SUCCESS\n at PORT : " + port + "\n at HOST : " + host
					+ " \n& ADDRESS : " + con.getAddress(), null, JOptionPane.INFORMATION_MESSAGE);
			break;
		case -1:
			JOptionPane.showMessageDialog(null, "\nUnable to Connect at port " + port
					+ "\nPossible ERRORS are..\n1. Port might not be opened for Connection on HOST:" + host
					+ "\n2. Host is invalid on local machine.\n3. Port is not valid." + " \n& ADDRESS : " + con.getAddress(),
					null, JOptionPane.ERROR_MESSAGE);
			break;
		case -2:
			JOptionPane.showMessageDialog(null, "\nERROR: Unbale to resolve Host adderess..", null, JOptionPane.ERROR_MESSAGE);
			break;
		case -3:
			JOptionPane.showMessageDialog(null, "\nERROR CODE:3\nUnExpected Error Occured.....\n Retry.", null,
					JOptionPane.ERROR_MESSAGE);
			break;
		case -4:
			JOptionPane.showMessageDialog(null, "\nEXCEPTION IN CLOSING CONNECTION..", null, JOptionPane.ERROR_MESSAGE);
			break;
		default:
			JOptionPane.showMessageDialog(null, "\nERROR CODE:4\nUnExpected Error Occured.....\n Retry.", null,
					JOptionPane.ERROR_MESSAGE);
			break;
		}
		return code;
	}

	public Messager(int port, String host) {

	}
}
