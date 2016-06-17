package com.view;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class ChessMenu extends JMenuBar {
	private static final long serialVersionUID = 2577157911075307707L;
	private JMenu file,help;
	@SuppressWarnings("unused")
	private JFrame frame;
	private JMenuItem file_logout,file_exit;
	private JMenuItem help_help,help_about;
	public ChessMenu(JFrame frame){
		this.frame = frame;
		initTheme();
		init();
	}
	private void initTheme(){
		
	}
	private void init(){
		file = new JMenu("File");
		file.setBackground(Color.white);
		file.setMnemonic('F');		
		file_logout = new JMenuItem("Login");
		file_logout.setMnemonic('L');
		file_logout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,KeyEvent.CTRL_DOWN_MASK));
		file_exit = new JMenuItem("Exit");
		file_exit.setMnemonic('x');
		file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_DOWN_MASK));
		file.add(file_logout);
		file.add(file_exit);		
		help = new JMenu("Help");
		help.setBackground(Color.white);
		help.setMnemonic('H');		
		help_help = new JMenuItem("Help Conetnets");
		help_help.setMnemonic('e');
		help_help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_DOWN_MASK));
		help_about = new JMenuItem("About");
		help_about.setMnemonic('u');
		help_about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,KeyEvent.CTRL_DOWN_MASK));
		help.add(help_help);
		help.add(help_about);
		add(file);
		add(help);
		setVisible(true);
	}
}
