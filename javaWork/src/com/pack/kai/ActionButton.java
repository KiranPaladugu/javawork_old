package com.pack.kai;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ActionButton implements ActionListener {
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Clear")) {
			// this.p.setText("");
			// this.h.setText("");
		}
		if (ae.getActionCommand().equals("Exit")) {
			System.exit(0);
		}
		if (ae.getActionCommand().equals("Test")) {
			Connector con = null;
			int port = 80;
			String host = "";
			// host=this.h.getText().trim();
			try {
				// port=Integer.parseInt(this.p.getText().trim());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Port Must be in Numbers Only", null, JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (port <= 0) {
				JOptionPane.showMessageDialog(null, "Port Must be Positive & non Zero  Numbers Only", null,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (host == null || host.equalsIgnoreCase("")) {
				JOptionPane.showMessageDialog(null, "Host Name cannot be Empty!", null, JOptionPane.ERROR_MESSAGE);
				return;
			}
			con = new Connector(port, host);
			int val = con.makeConnection();
			switch (val) {
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
				JOptionPane
						.showMessageDialog(null, "\nERROR: Unbale to resolve Host adderess..", null, JOptionPane.ERROR_MESSAGE);
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

		}
	}
}
