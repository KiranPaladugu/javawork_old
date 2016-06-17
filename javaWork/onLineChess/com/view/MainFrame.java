package com.view;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.reg.Register;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static MainFrame frame;
	private WelcomePanel welcome;
	public static MainFrame getFrame(){
		if(frame == null){
			frame = new MainFrame();
		}
		return frame;
	}
	public MainFrame() {
		setTheme();
		init();
	}
	private void setTheme() {
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void init(){
		ChessMenu menu =(ChessMenu)Register.getObject(ChessMenu.class);
		if(menu == null){
			Register.register(ChessMenu.class, new ChessMenu((MainFrame)Register.getObject(MainFrame.class)));
			menu = (ChessMenu) Register.getObject(ChessMenu.class);
		}		
		setJMenuBar(menu);
		welcome =(WelcomePanel)Register.getObject(WelcomePanel.class);
		if(menu == null){
			Register.register(WelcomePanel.class, new WelcomePanel((MainFrame)Register.getObject(MainFrame.class)));
			welcome = (WelcomePanel) Register.getObject(WelcomePanel.class);
		}
		welcome = new WelcomePanel(this);
		Register.printRegister();
		add(welcome);
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
