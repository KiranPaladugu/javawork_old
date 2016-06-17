package com.app.reminder.model;

public interface Event {
	public int getEventType();
	public Object getSource();
	public String getName();
	public Action[] getActions();
	public boolean hasAction();
}
