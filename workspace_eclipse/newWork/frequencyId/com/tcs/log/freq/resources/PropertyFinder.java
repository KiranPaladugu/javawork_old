package com.tcs.log.freq.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.framework.monitor.FileMoniter;
import com.logService.Logger;


public class PropertyFinder {
	protected  Properties properties;
	private static Properties tmpProps;
	private static InputStream input;
	private static File defaultFile=ResourceFinder.getResourceFile("app.properties");

	public PropertyFinder(Properties properties){
		this.properties = properties;
	}
	
	protected PropertyFinder(){
		
	}
	/**
	 * If file is null the loads defaults.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws PropertyFinderException
	 */
	public PropertyFinder(FileMoniter file){
		init(file);
	}
	private void init(FileMoniter file){
		try{
			getProperties(file);
		}catch(Exception e){
			properties = new Properties();
		}
	}
	public static PropertyFinder getPropertyFinder(String file){
		FileMoniter f = new FileMoniter(file);
		return new PropertyFinder(f);
	}
	public static PropertyFinder getPropertyFinder(FileMoniter file){		
		return new PropertyFinder(file);
	}
	public static PropertyFinder newInstance(String file){
		return new PropertyFinder(new FileMoniter(file));
		
	}
	public static PropertyFinder newInstance(FileMoniter file){
		return new PropertyFinder(file);
	}
	public static Properties getProperties(File file) throws PropertyFinderException, IOException {
		if (file == null) {
			if (tmpProps == null) {
				tmpProps = new Properties();
				if (file == null) {
					file = defaultFile;
				}
				input = new FileInputStream(file);
				tmpProps.load(input);
			}
			return tmpProps;
		} else {
			Properties props = new Properties();
			InputStream ins = new FileInputStream(file);
			props.load(ins);
			return props;
		}
	}
	public static Properties loadProperties(File file) throws PropertyFinderException{
		Properties props = null;
		
		props = new Properties();
		if (file == null) {
			throw new PropertyFinderException("File is null");
		}
		if(!file.exists()){
			file = ResourceFinder.getResourceFile(file.getName());
		}
		try {
			input = new FileInputStream(file);
			props.load(input);			
		} catch (FileNotFoundException e) {
			throw new PropertyFinderException(e);
		} catch (IOException e) {
			throw new PropertyFinderException(e);
		}

		return props;
	}

	public int getIntProperty(String key) throws PropertyFinderException {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public static int getIntProperty(String key, File file) throws PropertyFinderException {
		try {
			return Integer.parseInt(getProperties(file).getProperty(key));
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public static float getFloatProperty(String key, File file) throws PropertyFinderException {
		try {
			return Float.parseFloat(getProperties(file).getProperty(key));
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public float getFloatProperty(String key) throws PropertyFinderException {
		try {
			return Float.parseFloat(getProperty(key));
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public String getStringProperty(String key) throws PropertyFinderException {
		try {
			return getProperty(key);
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public static String getStringProperty(String key, File file) throws PropertyFinderException {
		try {
			return getProperties(file).getProperty(key);
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public String getProperty(String key) throws PropertyFinderException {
		try {
			return properties.getProperty(key);
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}
	public String getStringProperty(String key , String defaultValue){
		String value = defaultValue;
		try {
			value = getProperty(key);
			if(value == null || value.equals("")){
				value =defaultValue;
			}
		} catch (Exception e) {
			value = defaultValue;
		}
		return value;
	}

	public static String getProperty(String key, File file) throws PropertyFinderException {
		try {
			return getProperties(file).getProperty(key);
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public boolean getBoolProperty(String key, boolean defaultValue) {
		boolean flag = defaultValue;
		try {
			String property = getProperty(key);
			if ((property != null) && (!property.equalsIgnoreCase(""))) {
				if ((property.equalsIgnoreCase("yes")) || property.equalsIgnoreCase("true")
						|| property.equalsIgnoreCase("enable") || property.equalsIgnoreCase("enabled")) {
					flag = true;
				} else if ((property.equalsIgnoreCase("no")) || property.equalsIgnoreCase("false")
						|| property.equalsIgnoreCase("disable") || property.equalsIgnoreCase("disabled")) {
					flag = false;
				}
			}
		} catch (Exception e) {
		}
		return flag;
	}

	public int getIntProperty(String key, int defalutValue) {
		int value = defalutValue;
		try {
			value = getIntProperty(key);
		} catch (Exception e) {
		}
		return value;
	}

	public long getLongProperty(String key) throws PropertyFinderException {
		try {
			return Long.parseLong(getProperty(key));
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public long getLongProperty(String key, long defalutValue) {
		long value = defalutValue;
		try {
			value = getLongProperty(key);
		} catch (Exception e) {
		}
		return value;
	}

	public float getFloatProperty(String key, float defaultValue) {
		float value = defaultValue;
		try {
			value = getFloatProperty(key);
		} catch (Exception e) {
		}
		return value;
	}

	public double getDoubleProperty(String key) throws PropertyFinderException {
		try {
			return Double.parseDouble(getProperty(key));
		} catch (Exception e) {
			throw new PropertyFinderException(e);
		}
	}

	public double getDoubleProperty(String key, double defaultValue) {
		double value = defaultValue;
		try {
			value = getDoubleProperty(key);
		} catch (Exception e) {
		}
		return value;
	}
	
	public static String getProperty(String key,File file,String defaultValue){
		String value = defaultValue;
		try{
			String val= getProperties(file).getProperty(key);
			if((val != null)&&(!val.equals(""))){
				value = val;
			}
		}catch(Exception e){}
		return value;
	}
	public static String getProperty(String key,String file,String defaultValue){
		String value = defaultValue;
		try{
			File f = new File(file);
			String val= getProperties(f).getProperty(key);
			if((val != null)&&(!val.equals(""))){
				value = val;
			}
		}catch(Exception e){}
		return value;
	}
	public String getProperty(String key , String defalutString){
		String value = defalutString;
		try{
			String val = getProperty(key);
			if((val != null)&&(!val.equals(""))){
				value = val;
			}
		}catch (Exception e){}
		return value;
	}
	
	public static Properties getPropertiesFinder(File file)
		    throws IOException
		  {
		    if (file == null) {
		      if (tmpProps == null) {
		        Logger.log("Initalizing Properties Finder...");
		        tmpProps = new Properties();
		        Logger.log("Succesfully Initalizing Properties Finder...");
		        if (file == null) {
		          file = defaultFile;
		          Logger.log("setting file to Default cofig file...");
		        }
		        input = new FileInputStream(file);
		        tmpProps.load(input);
		      }
		      return tmpProps;
		    }
		    Properties props = new Properties();
		    Logger.log("Initalizing Properties ...");
		    InputStream ins = new FileInputStream(file);
		    props.load(ins);
		    Logger.log("Succesfully loaded Properties...");
		    return props;
		  }
	
	 public static int getIntPropertyWithName(String key) throws NumberFormatException, IOException {
		    return Integer.parseInt(getPropertiesFinder(null).getProperty(key));
		  }
		  public static int getIntPropertyWithName(String key, File file) throws NumberFormatException, IOException {
		    return Integer.parseInt(getPropertiesFinder(file).getProperty(key));
		  }
		  public static float getFloatPropertyWithName(String key, File file) throws NumberFormatException, IOException {
		    return Float.parseFloat(getPropertiesFinder(file).getProperty(key));
		  }
		  public static float getFloatPropertyWithName(String key) throws NumberFormatException, IOException {
		    return Float.parseFloat(getPropertiesFinder(null).getProperty(key));
		  }
		  public static String getStringPropertyWithName(String key) throws IOException {
		    return getPropertiesFinder(null).getProperty(key);
		  }
		  public static String getStringPropertyWithName(String key, File file) throws IOException {
		    return getPropertiesFinder(file).getProperty(key);
		  }
		  public static String getPropertyWithName(String key) throws IOException {
		    return getPropertiesFinder(null).getProperty(key);
		  }
		  public static String getPropertyWithName(String key, File file) throws IOException {
		    return getPropertiesFinder(file).getProperty(key);
		  }
}
