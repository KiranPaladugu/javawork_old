package com.tcs.berReader.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.framework.reg.Register;

public class UserMenuBar extends JMenuBar{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6385684710034925470L;
	private JMenuBar menubar;
	private JMenu file,help,options,view,edit,tools;
	private JMenuItem file_open,file_save,file_saveAs,file_exit;
	private JMenuItem help_about,help_contents;
	private JMenuItem options_preferences,options_updateLicense;
	private JMenuItem tools_create;
	//private JMenuItem edit_search,edit_copy,edit_selectAll;
	
	private UserOperations operations;
	
	public UserMenuBar() {
		init();
		operations = (UserOperations) Register.getObject(UserOperations.class);
		if(operations == null){
			operations = new UserOperations();
			Register.register(operations);
		}
	}
	
	public void init(){
		initMenu();
	}
	private void initMenu() {
		menubar = this;
		
		initFileMenu();
		initEditMenu();
		initViewMenu();
		initToolsMenu();
		initOptionsMenu();
		initHelpMenu();
		
		menubar.add(file);
		menubar.add(edit);
		menubar.add(view);
		menubar.add(tools);
		menubar.add(options);
		menubar.add(help);
		menubar.setVisible(true);
	}
	private void initToolsMenu() {
		tools = new JMenu("Tools");;
		
		tools_create = new JMenuItem("BERMaker");
		tools_create.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				operations.berMakerOperation();
			}
		});
		
		tools.add(tools_create);
	}

	private void initHelpMenu() {
		help = new JMenu("Help");
		help_contents = new JMenuItem("HelpContents");
		help_contents.addActionListener(new ActionListener() {			
			
			public void actionPerformed(ActionEvent e) {
				operations.showHelpContentsOpertaion();
			}
		});
		help_about = new JMenuItem("About");
		help_about.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				operations.showHelpAbout();
			}
		});
		
		help.add(help_contents);
		help.addSeparator();
		help.add(help_about);
		
	}
	private void initOptionsMenu() {
		options = new JMenu("Options");
		
		options_preferences = new JMenuItem("Preferences");
		options_preferences.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				operations.showPreferencesOperation();
			}
		});
		
		options_updateLicense = new JMenuItem("UpdateLicense");
		options_updateLicense.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				operations.updateLicenseOperation();
			}
		});
		
		
		options.add(options_preferences);
		options.addSeparator();
		options.add(options_updateLicense);
		
	}
	private void initViewMenu() {
		view = new JMenu("View");
	}
	private void initEditMenu() {
		edit = new JMenu("Edit");
	}
	private void initFileMenu() {
		file = new JMenu("File");
		
		file_open = new JMenuItem("Open");
		file_open.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent arg0) {
				operations.openFileOperation();
			}
		});
		file_save = new JMenuItem("Save");
		file_save.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				operations.saveFileOperation();
				
			}
		});
		file_saveAs = new JMenuItem("SaveAs");
		file_saveAs.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				operations.saveAsFileOperation();
				
			}
		});
		file_exit = new JMenuItem("Exit");
		file_exit.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				operations.applicationExit(true);
			}
		});
		
		file.add(file_open);
		file.addSeparator();
		file.add(file_save);
		file.add(file_saveAs);
		file.addSeparator();
		file.add(file_exit);
		
	}
	
	public String toString(){
		StringBuilder message=new StringBuilder("UserMenuBar");
		message.append("\n File:"+file);
		message.append("\n Edit:"+edit);
		message.append("\n view:"+view);
		message.append("\n Options:"+options);
		message.append("\n Help:"+help);
		return message.toString();
		
	}
}
