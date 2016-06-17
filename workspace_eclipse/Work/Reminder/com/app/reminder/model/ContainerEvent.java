package com.app.reminder.model;

public interface ContainerEvent extends Event {
	public static final int CONTAINER_START = 1;
	public static final int CONTAINER_DESTROY = 2;
	public static final int CONTAINER_CHANGED = 3;
	public static final int CONTAINER_ADDED =4;
	public static final int CONTAINER_REMOVED = 5;
	
	public Object getObject();
}
