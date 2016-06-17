package com.framework.monitor;

public class FileChangeHandler implements FileChangeListener {

	@Override
	public void fileChanged(FileEvent event) {
		System.out.println("File Modified : "+event.getFile().getName());
	}

	@Override
	public void fileCreated(FileEvent event) {
		System.out.println("File created : "+event.getFile().getName());
		
	}

	@Override
	public void fileDeleted(FileEvent event) {
		System.out.println("File Removed : "+event.getFile().getName());
		
	}

}
