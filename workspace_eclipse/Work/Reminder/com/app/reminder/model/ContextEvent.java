package com.app.reminder.model;

public interface ContextEvent extends Event {
	public static int PROPERTY_ADDED = 1;
	public static int PROPERTY_DELETED = 2;
	public static int CONTEXT_ADDED = 3;
	public static int CONTEXT_DELETED = 4;
	public static int CONTEXT_CHANGED = 5;
	
}
