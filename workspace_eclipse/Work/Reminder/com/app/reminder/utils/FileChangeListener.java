package com.app.reminder.utils;

public interface FileChangeListener {
	public void fileChanged(FileEvent event);

	public void fileCreated(FileEvent event);

	public void fileDeleted(FileEvent event);
}
