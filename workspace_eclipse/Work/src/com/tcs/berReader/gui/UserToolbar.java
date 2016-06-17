package com.tcs.berReader.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.framework.reg.Register;

public class UserToolbar extends JToolBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9073198970213831940L;
	private JButton open;
	private JToolBar toolbar;
	private UserOperations operations;
	public UserToolbar() {
		toolbar = this;
		operations = (UserOperations) Register.getObject(UserOperations.class);
		if(operations == null){
			operations = new UserOperations();
			Register.register(operations);
		}
		init();
		setVisible(false);	
	}

	private void init() {
		open = new JButton("op");
		open.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent arg0) {
				operations.openFileOperation();
			}
		});
		
		toolbar.add(open);
	}
}
