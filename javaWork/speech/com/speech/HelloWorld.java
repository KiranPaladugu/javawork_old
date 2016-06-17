package com.speech;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

public class HelloWorld {	
	private JFrame frame ;
	private JPanel panel ;
	private JPasswordField password ;
	private JLabel lbl;
	private String pass ;
	HelloWorld(){
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame = new JFrame();
		panel = new JPanel();
		password = new JPasswordField();
		lbl = new JLabel("Enter Current Password:");
		pass = "I am great";
		init();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private void init(){				
		Dimension d =Toolkit.getDefaultToolkit().getScreenSize();
		frame.setUndecorated(true);
		Container c =frame.getContentPane();
		panel.setLayout(null);
		lbl.setBounds((int)((d.getWidth()/2)-155),(int) (d.getHeight()-25)/2, 200, 25);
		password.setBounds((int)((d.getWidth()/2)), (int) (d.getHeight()-25)/2 , 200, 25);	
		password.addKeyListener(new KeyListener() {			
			public void keyTyped(KeyEvent e) {
				//System.out.println("Key Typed:"+e.getKeyCode()+" :char: "+e.getKeyChar());
			}
			
			public void keyReleased(KeyEvent e) {
				//System.out.println("Key Released:"+e.getKeyCode()+" :char: "+e.getKeyChar());
				getFoucus();
			}
			
			public void keyPressed(KeyEvent e) {
				//System.out.println("Key Pressed:"+e.getKeyCode()+" :char: "+e.getKeyChar());
				if(e.getKeyCode() ==27){
					quit();
				}
				if(e.getKeyCode()==10){
					authenticate();
				}
				getFoucus();
			}
		});	
		panel.add(lbl);
		panel.add(password);
		panel.setSize(d);		
		c.setLayout(null);
		panel.setOpaque(false);
		
		c.add(panel);
		frame.setSize(d);		
		frame.setVisible(true);
	}
	private void getFoucus(){
		frame.setFocusableWindowState(true);
		
	}
	private void authenticate(){

		if(getPassword().equals(pass)){
			System.out.println("correct Password!!");
			exit();
		}else if(getPassword().equals("cheat password")){
			System.out.println("You cheater..");
			JOptionPane.showMessageDialog(frame, "You cheater..");			
			exit();
		} else if(getPassword().equalsIgnoreCase("what is the password??!!@@**^^##")){ 
			JOptionPane.showMessageDialog(frame, pass);
			password.setText("");
		} else{
			System.out.println("Wrong Password!!");
			password.setText("");			
			JOptionPane.showMessageDialog(frame, "Wrong password !!", "Authentication Failed.", JOptionPane.ERROR_MESSAGE);
		}
	
	}
	private String getPassword(){
		return new String(password.getPassword());
	}
	private void quit(){
		JOptionPane.showMessageDialog(frame, "You can't Quit Unless You provide Password..", "Quit ???",JOptionPane.ERROR_MESSAGE);
	}
	private void exit(){
		System.exit(0);
	}
	public static void main(String args[]){
		new HelloWorld();
	}
}