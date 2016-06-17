package com.app.server;

import com.app.http.Container;
import com.app.http.Context;

public class GenericContainer implements Container {
	private ContainerContext context = new ContainerContext("GenericContext");
	private String name="";
	public Context getContainerContext() {		
		return context;
	}
	public String getName(){
		return name;
	}
}
