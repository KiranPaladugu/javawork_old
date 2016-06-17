package com.app.reminder.obj;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.app.reminder.model.Context;
import com.app.reminder.model.ContextEvent;
import com.app.reminder.model.ContextListener;

public class ApplicationContext implements Context {

	private Properties properties = new Properties();
	private Vector<ContextListener> listeners = new Vector<ContextListener>();
	private Map<String, Context> map = new HashMap<String, Context>();
	private String name;

	public String getContextName() {
		return name;
	}

	public Context[] getAll() {
		Object obj[] = map.values().toArray();
		Context con[] = new ApplicationContext[obj.length];
		try {
			for (int i = 0; i < obj.length; i++) {
				con[i] = (Context) obj[i];
			}
		} catch (Exception e) {
		}
		return con;
	}

	public void addPrperties(Properties properties) {
		this.properties.putAll(properties);
		notifyListener(ContextEvent.CONTEXT_CHANGED, this, "PROPERTY");
	}

	public Properties getProperties() {
		return properties;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void addContextListener(ContextListener listener) {
		listeners.add(listener);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void add(String name, Context context) {
		map.put(name, context);
		notifyListener(ContextEvent.CONTEXT_ADDED, this, "CONTEXT");
	}

	public void append(Context context) {

	}

	public Context getContext(String name) {
		return map.get(name);
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
		notifyListener(ContextEvent.CONTEXT_CHANGED, this, "PROPERTY");
	}

	private void notifyListener(int event, Object source, String name) {
		ContextEvent ce = new ContextEventImpl(event, source, name);
		for (ContextListener list : listeners) {
			list.ContextChanged(ce);
		}
	}

	public void removeListener(ContextListener listener) {
		listeners.remove(listener);
		notifyListener(ContextEvent.CONTEXT_CHANGED, this, "CONTEXT");
		
	}

	public void remoeALLListeners() {
		listeners.clear();
		notifyListener(ContextEvent.CONTEXT_CHANGED, this, "CONTEXT");
	}

	public void removeContext(String name) {
		if(map.remove(name)!=null){
			notifyListener(ContextEvent.CONTEXT_DELETED, this, "CONTEXT");
		}
		
		
	}

	public void removeAllContext() {
		map.clear();
		notifyListener(ContextEvent.CONTEXT_CHANGED, this, "CONTEXT");
	}

}
