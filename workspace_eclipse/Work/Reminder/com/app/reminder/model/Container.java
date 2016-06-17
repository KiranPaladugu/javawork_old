package com.app.reminder.model;

public interface Container {
	public void start();
	public void start(int code);
	public Context getContext(String name);
	public String getName();
	public void setName(String name);
	public void addContainerListener(ContainerListener listener);
	public void addContext(Context context);
	public void destroy();
	public void destroy(int code);
	public void add(String key,Container container);
	public Container getContainer(String name);
	public void removeContainer(String name);
	public void clearContainers();
	public void removeListener(ContainerListener listener);
	public void removeAllListeners();
}
