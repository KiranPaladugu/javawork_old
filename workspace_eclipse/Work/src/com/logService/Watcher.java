package com.logService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Watcher {
	private File file;
	private String fileName = "BERViewer.log";
	private boolean appender = false;
	private String path=null; 
	private FileWriter writer;
	private BufferedWriter bwriter;
	public boolean isDisabled;
	public boolean initalized = false;

	public Watcher() {
		
	}
	
	public void init(){
		String location = fileName;
		if(path != null){
			location = path+File.separator+fileName;
		}
		file = new File(location);
		if (!file.exists()) {
			try {
				File parent = file.getParentFile();
				if (parent !=null && !parent.getName().trim().equals("") && !parent.exists()) {
					parent.mkdirs();
				}
				file.createNewFile();
				System.out.println(">> Created log @ ..:" + file.getAbsolutePath());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Unable to create Log file..\nYour Problems were not logged.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
				isDisabled = true;
			}
		} else {
			System.out.println(">> Writing log @ ..:" + file.getAbsolutePath());
		}
		try {
			if (!isDisabled && file.exists())
				writer = new FileWriter(file, appender);
			bwriter = new BufferedWriter(writer);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to get Channel to Write to Log file..\nYour Problems were not logged.",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
			isDisabled = true;
		}
		initalized = true;
	}
	
	public FileWriter getWriter(){
		return writer;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public void setAppender(boolean flag){
		this.appender = flag;
	}

	public void log(String msg) {
		if(!initalized){
			init();
		}
		if (!isDisabled) {
			if (writer != null) {
				try {
					bwriter.append(msg);
					bwriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
