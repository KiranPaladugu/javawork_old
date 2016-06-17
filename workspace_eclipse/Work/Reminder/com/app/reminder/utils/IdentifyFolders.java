package com.app.reminder.utils;

import java.io.File;


public class IdentifyFolders {
	private static String root;
	
	public static String getApplicationRoot(){
		return findRoot();
	}

	private static String findRoot(){
		if (root == null || root.trim().equals("") || !root.contains(File.pathSeparator)) {
			File fi = new File("myname");
			String relativePath = fi.getAbsolutePath();
			int last = relativePath.lastIndexOf(File.separator);
			String classes = relativePath.substring(0, last);
			last = classes.lastIndexOf(File.separator);
			root = relativePath.substring(0, last);
		}
		if(root==null){
			root="";
		}
		return root;
	
	}

}
