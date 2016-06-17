package com.app.reminder.utils;

import java.io.File;

public interface FileEvent {
	public static final int FILE_CREATED = 1;
	public static final int FILE_REMOVED = -1;
	public static final int FILE_CHANGED = 3;
	public File getFile();
	public FileMoniter getWatcher();

}
