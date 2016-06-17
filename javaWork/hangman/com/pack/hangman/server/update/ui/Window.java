package com.pack.hangman.server.update.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Window extends JFrame implements WindowListener {
	private static final long serialVersionUID = 8277911755791790054L;

	public Window() {
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.ipadx = 10;
		gc.ipady = 10;
		gc.insets = new Insets(2, 2, 2, 2);
		Container c = this.getContentPane();
		c.setLayout(new GridBagLayout());
		c.add(new WindowPanel(), gc);
		this.pack();
		int wid = 350;
		int het = 450;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - wid) / 2;
		int y = (dim.height - het) / 2;
		setBounds(x, y, wid, het);
		this.setSize(wid, het);
		this.addWindowListener(this);
		this.pack();
		this.repaint();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// this.setResizable(false);
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosing(WindowEvent e) {
		if (!WindowPanel.saved) {
			int option = JOptionPane.showConfirmDialog(null, "Data not saved \n Do you want to save ?", "",
					JOptionPane.YES_NO_OPTION);
			if (option == 0) {
				System.exit(0);
			} else if (option == 1) {
				System.exit(0);
			}
		} else {
			System.exit(0);
		}
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
