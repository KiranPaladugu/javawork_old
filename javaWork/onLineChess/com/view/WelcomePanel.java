package com.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.logService.Logger;
import com.reg.Register;
import com.reg.RegisterException;
import com.resources.PropertyFinder;
public class WelcomePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel glassPane;
	private JPanel loginPanel;
	private JLabel user;
	private JLabel pass;
	private JTextField username;
	private JPasswordField password;
	private int spaceX;
	private int spaceY;
	private int cheight;
	private int cwidth;
	private int initialx=20;
	private int initialy=30;	
	private ResultPanel result;
	private JButton reset,close,login;
	private GridBagConstraints gc;
	public WelcomePanel(JFrame frame) {
		this.frame = frame;
		init();
	}
	private void init(){
		try {
			result = (ResultPanel) Register.getCheckedObject(ResultPanel.class);
		} catch (RegisterException re) {
			System.err.println("ResultPanel not Registered.");
			result = new ResultPanel();
		}
		glassPane = (JPanel)frame.getGlassPane();
		try{
			//backGround.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ResourceFinder.getResource("new.jpg"))));
		}catch(NullPointerException ne){
			Logger.log("Unable Load Background Image..");
		}						
		result.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		ChessBoard board = null;
		try {
			board = (ChessBoard) Register.getCheckedObject(ChessBoard.class);
		} catch (RegisterException re) {
			System.err.println("ResultPanel not Registered.");
			board = new ChessBoard();
		}
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridheight=1;
		gc.gridwidth=1;
		gc.weightx=1;
		gc.weighty=1;		
		add(board,gc);
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weightx=0.50;
		gc.weighty=1;
		add(result,gc);
		loginPanel = new JPanel();
		loginPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		user = new JLabel("User Name:");
		pass = new JLabel("Password:");
		username = new JTextField();
		password = new JPasswordField();
		login = new JButton("Login");
		reset = new JButton("Reset");
		close = new JButton("Close");
		glassPane.setLayout(null);
		loginPanel.setLayout(null);
		try {
			spaceX = PropertyFinder.getIntProperty("login.componentSpace.x");
			spaceY = PropertyFinder.getIntProperty("login.componentSpace.y");
			cheight = PropertyFinder.getIntProperty("login.componentHeight");
			cwidth = PropertyFinder.getIntProperty("login.componentWidht");
			loginPanel.setBounds(PropertyFinder.getIntProperty("login.panel.x"), PropertyFinder.getIntProperty("login.panel.y"), PropertyFinder.getIntProperty("login.panel.width"), PropertyFinder.getIntProperty("login.panel.height"));
			user.setBounds(initialx,initialy,cwidth,cheight);
			username.setBounds((initialx+cwidth+spaceX), initialy, cwidth+50, cheight);
			pass.setBounds(initialx, initialy+cheight+spaceY, cwidth, cheight);
			password.setBounds(initialx+cwidth+spaceX, initialy+cheight+spaceY, cwidth+50, cheight);
			cwidth = 75;
			initialx=07;
			login.setBounds(initialx, initialy+cheight+spaceY+cheight+spaceY, cwidth,cheight);
			reset.setBounds(initialx+cwidth+spaceX, initialy+cheight+spaceY+cheight+spaceY, cwidth,cheight);
			close.setBounds(initialx+cwidth+spaceX+cwidth+spaceX, initialy+cheight+spaceY+cheight+spaceY, cwidth,cheight);
			loginPanel.add(user);
			loginPanel.add(username);
			loginPanel.add(pass);
			loginPanel.add(password);
			loginPanel.add(login);
			loginPanel.add(reset);
			loginPanel.add(close);
			close.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					glassPane.setVisible(false);
				}
			});
			
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Wrong Initalization of property");
			e.printStackTrace();			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Unable to Load Config file..");
			e.printStackTrace();
		}			
		glassPane.add(loginPanel);
		frame.setGlassPane(glassPane);
		glassPane.setVisible(true);
	}
}
