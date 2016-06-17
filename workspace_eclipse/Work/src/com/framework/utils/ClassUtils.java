package com.framework.utils;

import java.lang.reflect.Constructor;

public class ClassUtils {
	public static Class<?> getClass(String className){
		Class<?> classs =null;
		try {
			classs = Class.forName(className);
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
		return classs;
	}
	public static Object getInstance(String className){
		Object obj=null;
		Class<?> c = getClass(className);
		obj = getInstance(c);
		return obj;
	}
	public static Object getInstance(Class<?> className){
		Object obj = null;
		try {
			obj = className.newInstance();
		} catch (InstantiationException e) {			
		} catch (IllegalAccessException e) {
		}
		return obj;
	}
	public static Object getInstance(String className,Object... ars){
		Object obj = null;
		Class<?> c = getClass(className);
		Constructor<?> []cons= c.getConstructors();
		for(Constructor<?> con:cons){
			System.out.println(con);
		}
		return obj;
	}
}
