package com.framework.reg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

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
				System.out.println("Class :" + className + " : is already Registered.");
			} else {
				System.out.println("Registering Class :" + className);
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
				// System.out.println("getting Object for :" + className);
				return map.get(className);
			} else {
				System.out.println(className + " is not Registered Yet.");
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
				System.out.println("deRegistering.. :" + className);
				map.remove(className);
				return true;
			} else {
				System.out.println("Unable to perform DeRegister for :" + className);
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
		System.out.println("deRegistering all..");
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
				throw new RegisterException("Unable to Intantiate");
			} catch (IllegalAccessException e) {
				throw new RegisterException("IllegalAccess");
			}
			return getObject(className);
		}
	}

	public static void printRegister() {
		System.out.println(getToString());
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
}
