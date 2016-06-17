package com.framework.gui.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AppConfig implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5207103504216144726L;
	private Map<String,ConfigValue> configMap = new HashMap<String, ConfigValue>(); 
	
	public Object getObject(String key){
		return configMap.get(key);
	}
	
	public Set<String> keySet(){
		return configMap.keySet();
	}
	
	public void putConfig(String key,ConfigValue value){
		configMap.put(key, value);				
	}
	
	public boolean containsKey(String key){
		return configMap.containsKey(key);
	}
	
	public boolean containsValue(ConfigValue value){
		return configMap.containsValue(value);
	}
	
	public Set<Entry<String, ConfigValue>> getEntrySet(){		
		return configMap.entrySet();
	}
	public boolean isEmpty(){
		return configMap.isEmpty();
	}
}
