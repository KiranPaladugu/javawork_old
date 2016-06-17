package com.app.reminder.model;

public interface ContainerListener {
	public void containerChanged(ContainerEvent ce);
	public void containerStart(ContainerEvent ce);
	public void containerDestroy(ContainerEvent ce);
	
}
