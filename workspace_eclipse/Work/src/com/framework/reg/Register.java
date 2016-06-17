package com.framework.reg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import com.logService.Logger;

public abstract class Register {
	private static Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
	private static int maxPermits = 1;
	public static Semaphore registerSem = new Semaphore(maxPermits);

	public static boolean register(Class<?> className, Object object) {
		boolean flag = false;
		try {
			registerSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (map.containsKey(className)) {
				Logger.log("Class :" + className.getSimpleName() + " : is already Registered!..");				
			} else {
				Logger.log("Registering Class :" + className.getSimpleName());
				map.put(className, object);
				flag = true;
			}
		} finally {
			registerSem.release();
		}
		return flag;
	}

	public static Object getObject(Class<?> className) {
		try {
			registerSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (map.containsKey(className)) {
				// Logger.log("getting Object for :" + className);
				return map.get(className);
			} else {
				Logger.log(className + " is not Registered Yet!..");
				return null;
			}
		} finally {
			registerSem.release();
		}
	}

	public static boolean deRegister(Class<?> className) {
		try {
			registerSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			if (map.containsKey(className)) {
				Logger.log("deRegistering.. :" + className.getSimpleName());
				map.remove(className);
				return true;
			} else {
				Logger.log("Unable to perform DeRegister for :" + className.getSimpleName());
				return false;
			}
		} finally {
			registerSem.release();
		}
	}

	public static void deRegisterAll() {
		try {
			registerSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Logger.log("deRegistering all!..");
		map.clear();
		registerSem.release();
	}

	public static Object getCheckedObject(Class<?> className) throws RegisterException {
		if (getObject(className) != null) {
			return getObject(className);
		} else {
			try {
				register(className, className.newInstance());
			} catch (InstantiationException e) {
				throw new RegisterException("Unable to Intantiate!..");
			} catch (IllegalAccessException e) {
				throw new RegisterException("IllegalAccess!!");
			}
			return getObject(className);
		}
	}
	
	public static void dump(){
		System.out.println(getToString());
	}

	public static void printRegister() {
		Logger.log(getToString());
	}

	public String toString() {
		return getToString();
	}

	private static String getToString() {
		try {
			registerSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Set<Class<?>> keys = map.keySet();
			String str = "";
			Iterator<Class<?>> itr = keys.iterator();
			str = "{";
			while (itr.hasNext()) {
				Class<?> item = itr.next();
				str += item.getName();
				str += "=" + map.get(item);
				if (itr.hasNext())
					str += ",";
			}
			str += "}";
			return str;
		} finally {
			registerSem.release();
		}
	}

	public static void register(Object object) {
		if (object != null) {
			register(object.getClass(), object);
		}
	}
	public static boolean oneTimeRegister(Object object){
		if(object!= null){
			if(map.containsKey(object.getClass())){
				return false;
			}else{
				map.put(object.getClass(), object);
				return true;
			}
		}
		return false;
	}
}
