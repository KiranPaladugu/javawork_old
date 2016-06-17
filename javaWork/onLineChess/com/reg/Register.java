package com.reg;

import java.util.HashMap;
import java.util.Map;
import com.logService.Logger;

public class Register {
	@SuppressWarnings("unchecked")
	private static Map<Class, Object> map = new HashMap<Class, Object>();

	@SuppressWarnings("unchecked")
	public static void register(Class className, Object object) {
		if (map.containsKey(className)) {
			Logger.log("Class :" + className + " : is already Registered.");
			return;
		} else {
			Logger.log("Registering Class :" + className);
			map.put(className, object);
		}
	}

	@SuppressWarnings("unchecked")
	public static Object getObject(Class className) {
		if (map.containsKey(className)) {
			Logger.log("getting Object for :" + className);
			return map.get(className);
		} else {
			Logger.log(className+" is not Registered Yet.");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static void deRegister(Class className) {
		if (map.containsKey(className)) {
			Logger.log("deRegistering.. :" + className);
			map.remove(className);
		}
		Logger.log("Unable to perform DeRegister for :" + className);
	}

	public static void deRegisterAll() {
		Logger.log("deRegistering all..");
		map.clear();
	}

	@SuppressWarnings("unchecked")
	public static Object getCheckedObject(Class className) throws RegisterException{
		if (getObject(className) != null) {
			return getObject(className);
		} else {
			try {
				register(className, className.newInstance());
			} catch (InstantiationException e) {
				throw new RegisterException("Unable to Intantiate");
			} catch (IllegalAccessException e) {
				throw new RegisterException("IllegalAccess");
			}
			return getObject(className);
		}
	}
	public static void printRegister(){
		System.out.println(map);
	}
}
