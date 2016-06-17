package com.app.reminder.utils;

public class FileChangeHandler implements FileChangeListener {

	public void fileChanged(FileEvent event) {
		System.out.println("File Modified : "+event.getFile().getName());
	}

	public void fileCreated(FileEvent event) {
		System.out.println("File created : "+event.getFile().getName());
		
	}

	public void fileDeleted(FileEvent event) {
		System.out.println("File Removed : "+event.getFile().getName());
		
	}

}
