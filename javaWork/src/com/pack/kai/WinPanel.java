package com.pack.kai;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class WinPanel extends JPanel implements ActionListener, KeyListener {
	/**
	 *
	 */
	GridBagConstraints c = new GridBagConstraints();
	JButton test;
	JButton Clear;
	JLabel port;
	JLabel host;
	JTextField p;
	JTextField h;
	JButton exit;
	private static final long serialVersionUID = 1L;

	public WinPanel() {
		Watcher.getWatcher().log("Initializing ....View");
		this.intialize();
		Watcher.getWatcher().mLog("DONE");
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Clear")) {
			this.p.setText("");
			this.h.setText("");
		}
		if (ae.getActionCommand().equals("Exit")) {
			System.exit(0);
		}
		if (ae.getActionCommand().equals("Test")) {
			connection();
		}
	}

	private void connection() {

		Connector con = null;
		int port = 80;
		String host = null;
		host = this.h.getText().trim();
		try {
			port = Integer.parseInt(this.p.getText().trim());
		} catch (NumberFormatException e) {
			Watcher.getWatcher().mLog("Invalid Port : Port Must be a Number");
			JOptionPane.showMessageDialog(this, "Port Must be in Numbers Only", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (port <= 0) {
			Watcher.getWatcher().mLog("Invalid Port : Port can not be negative or Zero");
			JOptionPane
					.showMessageDialog(this, "Port Must be Positive & non Zero  Numbers Only", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (host == null || host.equalsIgnoreCase("")) {
			Watcher.getWatcher().mLog("Invalid HOST : Host name is Empty !");
			JOptionPane.showMessageDialog(this, "Host Name cannot be Empty!", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
		con = new Connector(port, host);
		int val = con.makeConnection();
		switch (val) {
		case 0:
			JOptionPane.showMessageDialog(this, "Connection SUCCESS\n at PORT : " + port + "\n at HOST : " + host
					+ " \n& ADDRESS : " + con.getAddress(), null, JOptionPane.INFORMATION_MESSAGE);
			break;
		case -1:
			JOptionPane.showMessageDialog(this, "\nUnable to Connect at port " + port
					+ "\nPossible ERRORS are..\n1. Port might not be opened for Connection on HOST:" + host
					+ "\n2. Host is invalid on local machine.\n3. Port is not valid." + " \n& ADDRESS : " + con.getAddress(),
					null, JOptionPane.ERROR_MESSAGE);
			break;
		case -2:
			JOptionPane.showMessageDialog(this, "\nERROR: Unbale to resolve Host adderess..", null, JOptionPane.ERROR_MESSAGE);
			break;
		case -3:
			JOptionPane.showMessageDialog(this, "\nERROR CODE:3\nUnExpected Error Occured.....\n Retry.", null,
					JOptionPane.ERROR_MESSAGE);
			break;
		case -4:
			JOptionPane.showMessageDialog(this, "\nEXCEPTION IN CLOSING CONNECTION..", null, JOptionPane.ERROR_MESSAGE);
			break;
		default:
			JOptionPane.showMessageDialog(this, "\nERROR CODE:4\nUnExpected Error Occured.....\n Retry.", null,
					JOptionPane.ERROR_MESSAGE);
			break;
		}

	}

	private void intialize() {
		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY);
		this.setBorder(border);
		c = new GridBagConstraints();
		test = new JButton("Test");
		Clear = new JButton("Clear");
		port = new JLabel("  PORT : ");
		port.setFont(new Font("Arial", Font.BOLD, 12));
		host = new JLabel("  HOST : ");
		host.setFont(new Font("Arial", Font.BOLD, 12));
		p = new JTextField("80");
		h = new JTextField("localhost");
		exit = new JButton("Exit");
		this.setLayout(new GridBagLayout());
		// h.setSize(150, 30);
		this.setSize(400, 500);

		c.weightx = c.weighty = 1.0;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2, 2, 2, 2);
		this.add(port, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.RELATIVE;
		this.add(p, c);

		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		this.add(host, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 2;
		c.gridy = 1;
		this.add(h, c);
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 2;

		this.add(test, c);
		/**
		 * c.fill = GridBagConstraints.HORIZONTAL; c.ipady = 0; //reset to
		 * default c.weighty = 1.0; //request any extra vertical space c.anchor
		 * = GridBagConstraints.PAGE_END; //bottom of space c.insets = new
		 * Insets(10,0,0,0); //top padding c.gridx = 1; //aligned with button 2
		 * c.gridwidth = 2; //2 columns wide c.gridy = 2; //third row
		 */
		c.gridx = 4;
		c.gridy = 2;
		test.addActionListener(this);
		Clear.addActionListener(this);
		this.add(Clear, c);
		c.gridx = 6;
		c.gridy = 2;
		this.add(exit, c);
		exit.addActionListener(this);
		this.setVisible(true);
		this.repaint();
		this.p.addKeyListener(this);
		this.h.addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			System.out.println("You Pressed ENTER.");
			connection();
		}

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
