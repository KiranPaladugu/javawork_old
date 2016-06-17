package com.framework.monitor;

public class Tester {
	public static void main(String[] args) {
		FileMoniter watcher = new FileMoniter("C:\\Users\\ekirpal\\Documents\\New Folder\\newFile.txt",2000);
		watcher.addFileChangeListener(new FileChangeHandler());		
		watcher.startMointor();
		watcher.setMonitorName("My Name is Thread");
		System.out.println(watcher.getFile().getClass());
		System.out.println(watcher.getFreeSpace());
		System.out.println(watcher.getTotalSpace() - watcher.getFreeSpace());		
	}
}
