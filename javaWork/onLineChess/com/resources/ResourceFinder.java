package com.resources;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import com.logService.Logger;

public class ResourceFinder {
	public static URL getResource(){
		return ResourceFinder.getResource();		
	}
	public static URL getResource(String name){
		try{
		return ResourceFinder.class.getResource(name);
		}catch (Exception e){
			Logger.log("Unable to Load resource:"+name);
			return null;
		}
	}
	public static File getResourceFile(String name){
		return new File(ResourceFinder.class.getResource(name).getFile());
	}
	public static InputStream getResouceAsAStream(String name){
		return ResourceFinder.class.getResourceAsStream(name);
	}
}
