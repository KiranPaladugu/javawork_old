package com.framework.monitor;

import java.io.File;
import java.net.URI;

public class FileMoniter extends File {

	private static final long serialVersionUID = -7093586115623489148L;
	private long millies = -1;
	private boolean monitor = false;
	private FileChangeListener listerner;
	private Watcher watcher;

	public FileMoniter(String pathname) {
		super(pathname);
	}

	public FileMoniter(File parent, String child) {
		super(parent, child);
	}

	public FileMoniter(String parent, String child) {
		super(parent, child);

	}

	public FileMoniter(URI uri) {
		super(uri);

	}

	/**
	 * Creates new instance of file using file. and then monitors FileMoniter.
	 * 
	 * @param file
	 * @param delay
	 */
	public FileMoniter(File file, int delay) {
		super(file.getName());
		this.millies = delay;
	}

	/**
	 * 
	 */
	public void startMointor() {
		if (!monitor) {
			monitor = true;
			watcher = new Watcher();
		} else {
			throw new IllegalStateException("Monitoring already started");
		}
	}

	/**
	 * @param pathname
	 */
	public FileMoniter(String pathname, long millies) {
		super(pathname);
		this.millies = millies;
	}

	public void addFileChangeListener(FileChangeListener listener) {
		this.listerner = listener;
	}

	public void removeFileChangeListener() {
		this.listerner = null;
	}

	private class Calller extends Thread {		
		private int type = 0;
		private FileChangeListener lis;

		public Calller(int type, FileChangeListener listener) {
			this.type = type;
			lis = listener;
			this.start();
		}

		public void run() {
			switch (type) {
			case FileEvent.FILE_CREATED:
				lis.fileCreated(new WatcherEvent());
				break;
			case FileEvent.FILE_REMOVED:
				lis.fileDeleted(new WatcherEvent());
				break;
			case FileEvent.FILE_CHANGED:
				lis.fileChanged(new WatcherEvent());
				break;
			default:
				break;
			}
		}
	}

	private class Watcher extends Thread {

		private boolean run = true;
		private boolean created_old = get().exists();
		private boolean created_new = created_old;
		private boolean removed_old = !created_old;
		private boolean removed_new = removed_old;
		private long lastModified_old = get().lastModified();
		private long lenght_old = get().length();
		private long lastModified_new = lastModified_old;
		private long lenght_new = lenght_old;

		Watcher() {
			if (get() != null) {
				this.setName(get().getName());
				this.start();
			} else {
				System.out.println("File Not Found !!");
			}
		}

		public void run() {
			System.out.println("Starting Thread.." + run);
			synchronized (listerner) {
				while (run) {
					if (get() != null) {
						if (millies > 0) {
							if (listerner != null) {
								if (isCreated()) {
									new Calller(FileEvent.FILE_CREATED, listerner);
								} else if (isRemoved()) {
									new Calller(FileEvent.FILE_REMOVED, listerner);
								} else if (isChanged()) {
									new Calller(FileEvent.FILE_CHANGED, listerner);
								}
								try {
									Thread.sleep(millies);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} else {
								System.out.println("Listener is null ");
								run = false;
							}
						} else {
							System.out.println("millies are" + millies);
							run = false;
						}
					} else {
						System.out.println("File Not Found !!");
					}
				}
				System.out.println("Ending Thread.." + run);
			}
			monitor = false;
		}

		public boolean isCreated() {
			boolean flag = false;
			created_new = get().exists();
			if (!created_old && created_new)
				flag = true;
			else
				flag = false;
			created_old = created_new;
			if (flag) {
				lastModified_old = lastModified_new = 0;
				lenght_old = lenght_new = 0;
			}
			// System.out.println("isCreated : " + flag);
			return flag;
		}

		public boolean isRemoved() {
			removed_new = !get().exists();
			boolean flag = false;
			if (!removed_old && removed_new)
				flag = true;
			else
				flag = false;
			removed_old = removed_new;
			if (flag) {
				lastModified_old = lastModified_new = 0;
				lenght_old = lenght_new = 0;
			}
			// System.out.println("isRemoved : " + flag);
			return flag;
		}

		public boolean isChanged() {
			lastModified_new = get().lastModified();
			boolean flag = false;
			lenght_new = get().length();
			if ((lastModified_new > lastModified_old) || (lenght_new > lenght_old) || (lenght_new < lenght_old))
				flag = true;
			else
				flag = false;
			lastModified_old = lastModified_new;
			lenght_old = lenght_new;
			// System.out.println("isChanged : " + flag);
			return flag;
		}
	}

	public class WatcherEvent implements FileEvent {

		@Override
		public File getFile() {
			return get();
		}

		@Override
		public FileMoniter getWatcher() {
			return get();
		}

	}

	private FileMoniter get() {
		return this;
	}

	public File getFile() {
		return get();
	}

	public void setMonitorName(String name) {
		if (watcher != null) {
			watcher.setName(name);
		}
	}

	public String getMonitorName() {
		if (watcher != null) {
			return watcher.getName();
		} else {
			return "";
		}
	}
}
