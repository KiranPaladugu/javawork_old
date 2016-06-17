package com.pack.hangman.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class HangmanMenu extends JMenuBar implements MenuListener, MouseListener {
	private static final long serialVersionUID = 7939069692842337813L;
	private JMenu file;
	private JMenuItem exit;
	private JMenuItem startNew;
	private JMenu help;
	private JMenuItem helpContents;
	private JMenuItem about;

	public HangmanMenu() {
		this.init();
	}

	public void init() {
		this.setVisible(true);
		file = new JMenu("File");
		exit = new JMenuItem("Exit");
		startNew = new JMenuItem("Start New");
		help = new JMenu("Help");
		helpContents = new JMenuItem("Help Contents");
		about = new JMenuItem("About");
		about.addMouseListener(this);
		file.add(startNew);
		file.addSeparator();
		file.add(exit);
		add(file);

		help.add(helpContents);
		help.addSeparator();
		help.add(about);
		add(help);
	}

	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		if (e.getSource().equals(about)) {
			String msg = "Hangman all rights reserved.\nAll the content is copyWrited.";
			JOptionPane.showMessageDialog(null, msg, "About", JOptionPane.INFORMATION_MESSAGE);
		}

	}
}
