package com.app.server;

import com.app.http.Context;


public class ContainerContext implements Context{
	public String name;
	public String path;
	public ContainerContext(String name) {
		this.name=name;
	}
	public String getContextParameter(String parameter) {		
		return null;
	}
	public String parameter(String parameter) {
		return null;
	}
	public String getName(){
		return name;
	}
	public String getContextPath() {		
		return null;
	}

}
