package com.app.reminder.obj;

import com.app.reminder.model.Action;
import com.app.reminder.model.Event;

public class GenericEvent implements Event {
	private int event = -1;
	private Object source;
	private String name;
	
	

	public GenericEvent(int event, Object source, String name) {
		super();
		this.event = event;
		this.source = source;
		this.name = name;
	}

	public int getEventType() {
		return event;
	}

	public Object getSource() {
		return source;
	}

	public String getName() {
		return name;
	}

	public Action[] getActions() {
		return null;
	}

	public boolean hasAction() {
		return false;
	}

}
