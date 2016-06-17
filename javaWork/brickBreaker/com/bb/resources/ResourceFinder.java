package com.bb.resources;

import java.io.InputStream;
import java.net.URL;

public class ResourceFinder {
	public static URL getResource(){
		return ResourceFinder.getResource();		
	}
	public static URL getResource(String name){
		return ResourceFinder.class.getResource(name);
	}
	public static InputStream getResourceAsAStream(String name){
		return ResourceFinder.class.getResourceAsStream(name);
	}
}
