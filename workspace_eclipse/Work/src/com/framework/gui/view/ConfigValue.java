package com.framework.gui.view;

import java.io.Serializable;

public class ConfigValue implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 337816070271534898L;
	private Object obj = null;
	
	public ConfigValue(Object obj) {
		this.obj = obj;
	}
	
	public String getClassName(){
		if(obj != null){
			return obj.getClass().getName();
		}else{
			return null;
		}
	}
	
	public String getSimpleName(){
		if(obj != null){
			return obj.getClass().getSimpleName();
		}else{
			return null;
		}
	}
	
	public Object getObject(){
		return obj;
	}

}
