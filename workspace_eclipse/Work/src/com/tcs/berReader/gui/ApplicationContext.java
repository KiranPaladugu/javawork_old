package com.tcs.berReader.gui;

import java.io.File;
import java.util.Properties;

import com.framework.gui.view.AppConfig;
import com.logService.Logger;
import com.tcs.ber.resource.PropertyFinder;
import com.tcs.ber.resource.PropertyFinderException;
import com.tcs.tmp.ConfigHandler;

public class ApplicationContext {
	private static PropertyFinder properties;
	private static PropertyFinder appProperties;
	private static AppConfig configuraiton;
	private static volatile transient boolean isLoading = false;
	private static String root;
	private static String lib;
	private static String conf;
	private static String log;
	private static String bin;
	
	public static void setLoading(boolean value){
		isLoading = value;
	}
	
	public static boolean isLoading(){
		return isLoading;
	}
	
	
	public static String getConf() {		
			try {
				conf = getRoot()+File.separator+getApplicationProfile().getProperty("BERViewer.application.folder.conf");
			} catch (Exception e) {
				Logger.log("Unable to get ROOT path and CONF path");
				conf="";
			}		
		return conf;
	}
	public static String getRoot() {
		return findRoot();
	}
	public static String getLib() {
		try {
			lib = getRoot()+File.separator+getApplicationProfile().getProperty("BERViewer.application.folder.lib");
		} catch (PropertyFinderException e) {
			Logger.log("Unable to get ROOT path and LIB path");
			lib="";
		}		
		return lib;
	}
	public static String getLog() {
		try {
			log = getRoot()+File.separator+getApplicationProfile().getProperty("BERViewer.application.folder.log");
		} catch (PropertyFinderException e) {
			Logger.log("Unable to get ROOT path and LOG path");
			log="";
		}	
		return log;
	}
	public static String getBin() {
		try {
			bin = getRoot()+File.separator+getApplicationProfile().getProperty("BERViewer.application.folder.bin");
		} catch (PropertyFinderException e) {
			Logger.log("Unable to get ROOT path and BIN path");
			bin="";
		}	
		return bin;
	}
	
	
	private static String findRoot(){
		if (root == null || root.trim().equals("") || !root.contains(File.pathSeparator)) {
			File fi = new File("myname");
			String relativePath = fi.getAbsolutePath();
			int last = relativePath.lastIndexOf(File.separator);
			Logger.log(relativePath);
			String classes = relativePath.substring(0, last);
			last = classes.lastIndexOf(File.separator);
			root = relativePath.substring(0, last);
			Logger.log("ROOT :"+root);
		}
		if(root==null){
			root="";
			Logger.log("ROOT :"+root);
		}
		return root;
	}
	
	public static PropertyFinder getApplicationProfile(){
		if(properties == null){
			try {
				Properties props = PropertyFinder.getPropertiesFinder(null);
				properties = new PropertyFinder(props);
			} catch (Exception e) {
				properties = new PropertyFinder(new Properties());
			}
		}
		return properties;
	}
	public static PropertyFinder getApplicationProperties(){
		if(appProperties == null){
			try {
				File file = new File(getConf()+File.separator+getApplicationProfile().getProperty("BERViewer.application.file.name.properties"),"");
				Properties props = PropertyFinder.getPropertiesFinder(file);
				appProperties = new PropertyFinder(props);
			} catch (Exception e) {
				Logger.log("Unable to load properties .. :"+getApplicationProfile().getProperty("BERViewer.application.file.name.properties",""));
				appProperties = new PropertyFinder(new Properties());
			} 
		}
		return appProperties;
	}
	public static AppConfig getApplicationConfiguration(){
		if(configuraiton == null){
			ConfigHandler handler = new ConfigHandler();
			configuraiton = handler.loadConfig();
		}
		return configuraiton;
	}
	public static void setAppConfig(AppConfig config){
		configuraiton = config;
	}

}
