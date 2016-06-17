package com.app.reminder.model;

import java.util.Properties;


public interface Context {
	public void setName(String name);
	public String getContextName();
	public void add(String name,Context context);
	public void append(Context context);
	public Context getContext(String name);
	public Context[] getAll();
	public void setProperties(Properties properties);
	public void addPrperties(Properties properties);
	public Properties getProperties();
	public String getProperty(String key);
	public void addContextListener(ContextListener listener);
	public void removeListener(ContextListener listener);
	public void remoeALLListeners();
	public void removeContext(String name);
	public void removeAllContext();
}
