package com.app.res;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import com.resources.ResourceFinder;

public class Resources {
	public static URL getResouce(String name){
		return Resources.class.getClassLoader().getResource(name);
	}
	public static Enumeration<URL> getResource(String name) throws IOException{
		return Resources.class.getClassLoader().getResources(name);
	}
	public static File getResourceFile(String name){
		return new File(ResourceFinder.class.getResource(name).getFile());
	}
}
