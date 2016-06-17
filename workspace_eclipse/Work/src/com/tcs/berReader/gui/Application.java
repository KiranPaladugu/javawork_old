package com.tcs.berReader.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Application implements PropertyChangeListener {

	private static Application app;
	
	public static final Application getInstance(){
		if(app == null){
			app = new Application();
		}		
		return app;
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			ApplicationEvent event = (ApplicationEvent) evt.getNewValue();
			switch (event.getEvent()) {
			case ApplicationEvent.APPLICATION_EXIT : shutDown();break;
			case ApplicationEvent.APPLICATION_START : start();break;
			case ApplicationEvent.APPLICATION_STARTED : started();break;
			}
		} catch (Exception e) {

		}
	}

	private void started() {
		
	}

	private void start() {
		
	}

	private void shutDown() {
		
	}
	
}
