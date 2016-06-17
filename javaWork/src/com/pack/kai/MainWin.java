package com.pack.kai;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class MainWin extends JFrame implements WindowListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String args[]) {
		Watcher.getWatcher().headLog("Starting Application..");
		new MainWin();
	}

	public MainWin() {
		init();
		WinPanel wp = new WinPanel();
		this.add(wp);
		this.repaint();
		this.setLayout(new GridLayout(0, 1));
		// this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("  PORT TESTER");
		this.pack();
		int wid = 250;
		int het = 130;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - wid) / 2;
		int y = (dim.height - het) / 2;
		setBounds(x, y, wid, het);
		this.setSize(wid, het);
		this.setVisible(true);
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWin.class.getResource("pt.GIF")));
		this.addWindowListener(this);
	}

	private void init() {
		Watcher.getWatcher().headLog("Setting Look and Feel to Windows.");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosing(WindowEvent e) {
		int x = JOptionPane.showConfirmDialog(null, "Do You want to exit ?", "CONFIRM", JOptionPane.YES_NO_OPTION);
		if (x == 0)
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
