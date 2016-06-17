package com.app.reminder.model;

public interface TimeListener {
	public void hourStart(TimeEvent event);
	public void hourEnd(TimeEvent event);
	public void hourChanged(TimeEvent event);
	
	public void minuteStart(TimeEvent event);
	public void minuteEnd(TimeEvent event);
	public void minuteChanged(TimeEvent event);
	
	public void dayStart(TimeEvent event);
	public void dayEnd(TimeEvent event);
	public void dayChanged(TimeEvent event);
	
	public void weekStart(TimeEvent event);
	public void weekEnd(TimeEvent event);
	public void weekChanged(TimeEvent event);
	
	public void monthStart(TimeEvent event);
	public void monthEnd(TimeEvent event);
	public void monthChanged(TimeEvent event);
	
	public void yearStart(TimeEvent event);
	public void yearEnd(TimeEvent event);
	public void yearChanged(TimeEvent event);
}
