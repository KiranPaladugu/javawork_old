package newapp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Register {
	@SuppressWarnings("unchecked")
	private static Map<Class, Object> map = new HashMap<Class, Object>();

	@SuppressWarnings("unchecked")
	public static boolean register(Class className, Object object) {
		if (map.containsKey(className)) {
			System.out.println("Class :" + className + " : is already Registered.");
			return false;
		} else {
			System.out.println("Registering Class :" + className);
			map.put(className, object);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public static Object getObject(Class className) {
		if (map.containsKey(className)) {
			System.out.println("getting Object for :" + className);
			return map.get(className);
		} else {
			System.out.println(className + " is not Registered Yet.");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean deRegister(Class className) {
		if (map.containsKey(className)) {
			System.out.println("deRegistering.. :" + className);
			map.remove(className);
			return true;
		} else {
			System.out.println("Unable to perform DeRegister for :" + className);
			return false;
		}
	}

	public static void deRegisterAll() {
		System.out.println("deRegistering all..");
		map.clear();
	}

	@SuppressWarnings("unchecked")
	public static Object getCheckedObject(Class className) throws RegisterException {
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

	@SuppressWarnings("unchecked")
	private static String getToString() {
		Set<Class> keys = map.keySet();
		String str = "";
		Iterator<Class> itr = keys.iterator();
		str = "{";
		while (itr.hasNext()) {
			Class<?> item = itr.next();
			str += "\t" + item.getName();
			str += "," + map.get(item);
		}
		str += "{";
		return str;
	}
}
