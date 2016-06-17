package com.app.reminder.model;

import java.util.Date;

public interface TimeEvent extends Event {
	
	public Date getDate();
	public Object getObject();
}
