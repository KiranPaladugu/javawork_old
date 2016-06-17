package com.pack.hangman.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.pack.hangman.log.Logger;

public class HangmanFrame extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1559470050700839051L;

	public HangmanFrame() {
		init();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void init() {
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLayout(new GridLayout());
		this.setTitle("H A N G M A N");
		this.addWindowListener(this);
		Container c = this.getContentPane();
		// this.setJMenuBar(new HangmanMenu());
		c.add(new FrontPanel(this));
		// Logger.log("setting Menubar");
		this.repaint();
		int wid = 350;
		int het = 350;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - wid) / 2;
		int y = (dim.height - het) / 2;
		setBounds(x, y, wid, het);
		this.setSize(wid, het);
		this.repaint();
		Graphics g = getGraphics();
		this.paint(g);
		this.setVisible(true);
		this.setResizable(false);
		Logger.log("Visibility setting success...");
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent e) {
		Logger.log("Closing log...");
		Logger.closeLog();
		System.exit(0);

	}

	public void windowClosing(WindowEvent e) {
		Logger.log("Closing LOG ...");
		Logger.closeLog();
		System.exit(0);

	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
