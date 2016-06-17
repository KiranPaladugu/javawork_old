package com.framework.monitor;

public interface FileChangeListener {
	public void fileChanged(FileEvent event);

	public void fileCreated(FileEvent event);

	public void fileDeleted(FileEvent event);
}
