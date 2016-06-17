package com.app.reminder.obj;

import com.app.reminder.model.Action;
import com.app.reminder.model.ContainerEvent;

public class ContainerEventImp implements ContainerEvent {
	private int event = -1;
	private Object source;
	private String name;
	private Object object;
	

	public ContainerEventImp(int event, Object source, String name,Object object) {
		super();
		this.event = event;
		this.source = source;
		this.name = name;
		this.object = object;
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

	public Object getObject() {
		return object;
	}

}
