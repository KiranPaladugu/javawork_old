package com.framework.reg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class NameRegister {
	
	private static Map<String, Object> map = new HashMap<String, Object>();

	public static boolean register(String key, Object object) {
		if (map.containsKey(key)) {
			System.out.println("Object already present for key :" + key);
			return false;
		} else {
			System.out.println("Adding Value and Key for key :" + key);
			map.put(key, object);
			return true;
		}
	}

	public static Object getObject(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			System.out.println("No Objects Found with key :" + key);
			return null;
		}
	}

	public static boolean deRegister(String key) {
		if (map.containsKey(key)) {
			System.out.println("Removing and Object and Key for key :" + key);
			map.remove(key);
			return true;
		} else {
			System.out.println("Unable to perform DeRegister for key:" + key);
			return false;
		}
	}

	public static void deRegisterAll() {
		System.out.println("Removing all..");
		map.clear();
	}

	public static boolean overRideValue(String key, Object object) {
		if (register(key, object)) {
			return false;
		} else {
			Object obj = map.put(key, object);
			if (obj != null) {
				System.out.println("Overrided key :" + key);
				return true;
			} else {
				System.out.println("Not OverRided ... But added key :" + key);
				return false;
			}
		}
	}

	public String toString() {
		return getToString();
	}

	private static String getToString() {
		Set<String> keys = map.keySet();
		String str = "";
		Iterator<String> itr = keys.iterator();
		str = "{";
		while (itr.hasNext()) {
			String item = itr.next();
			str += item;
			str += "=" + map.get(item);
			if (itr.hasNext())
				str += ",";
		}
		str += "}";
		return str;
	}
}
