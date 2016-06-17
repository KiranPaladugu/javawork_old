package com.tcs.berReader.gui;

public class ApplicationEvent {
	public static final int APPLICATION_EXIT = 0 ;
	public static final int APPLICATION_START = 1;
	public static final int APPLICATION_STARTED = 2;	
	private int event=-1;
	private String name="NO_NAME";
	public void setEvent(int event) {
		this.event = event;
	}
	
	public int getEvent() {
		return event;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
