package com.app.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.logService.Logger;

public class PropertyFinder {
	private static Properties properties;
	@SuppressWarnings("unused")
	private static File file;
	private static File defaultFile=Resources.getResourceFile("appConfig.properties");
	private static InputStream input;
	/**
	 * If file is null the loads defaults.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Properties getPropertiesFinder(File file) throws IOException{
		if(properties == null){
			Logger.log("Initalizing Properties Finder...");
			properties = new Properties();
			Logger.log("Succesfully Initalizing Properties Finder...");
			if(file==null){
				file = defaultFile;
				Logger.log("setting file to Default cofig file...");
			}
			input = new FileInputStream(file);
			properties.load(input);
		}
		return properties;
	}
	public static int getIntProperty(String key) throws NumberFormatException, IOException{
		return Integer.parseInt(getPropertiesFinder(null).getProperty(key));
	}
	public static int getIntProperty(String key,File file) throws NumberFormatException, IOException{
		return Integer.parseInt(getPropertiesFinder(file).getProperty(key));
	}
	public static float getFloatProperty(String key,File file) throws NumberFormatException, IOException{		
		return Float.parseFloat(getPropertiesFinder(file).getProperty(key));
	}
	public static float getFloatProperty(String key) throws NumberFormatException, IOException{		
		return Float.parseFloat(getPropertiesFinder(null).getProperty(key));
	}
	public static String getStringProperty(String key) throws IOException{	
		return getPropertiesFinder(null).getProperty(key);
	}
	public static String getStringProperty(String key,File file) throws IOException{	
		return getPropertiesFinder(file).getProperty(key);
	}
	public static String getProperty(String key) throws IOException{	
		return getPropertiesFinder(null).getProperty(key);
	}
	public static String getProperty(String key,File file) throws IOException{		
		return getPropertiesFinder(file).getProperty(key);
	}
}
