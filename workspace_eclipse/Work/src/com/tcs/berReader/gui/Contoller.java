package com.tcs.berReader.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.framework.reg.Register;

public class Contoller extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1763094037000706996L;
	private JPanel panel;
	private JButton addBer;
	private JButton addFolder;
	private JButton load;
	private UserOperations operations;
	 
	
	public Contoller() {
		panel = this;
		operations = (UserOperations) Register.getObject(UserOperations.class);
		if(operations == null){
			operations = new UserOperations();
			Register.register(operations);
		}
		init();
	}
	public void init(){
		addItems();
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}
	private void addItems() {
		addBer= new JButton("AddBER");
		addBer.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent arg0) {
				operations.openFileOperation();
			}
		});
		
		addFolder = new JButton("AddFolder");
		addFolder.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				operations.openFolderOperation();
			}
		});
		load = new JButton("load");
		load.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				operations.loadOpertation();
			}
		});
		
		panel.add(addBer);
		panel.add(addFolder);
		panel.add(load);
	}
}
