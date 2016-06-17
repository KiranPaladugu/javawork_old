package reg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Register {
	private static Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();

	public static boolean register(Class<?> className, Object object) {
		if (map.containsKey(className)) {			
			return false;
		} else {
			map.put(className, object);
			return true;
		}
	}

	public static Object getObject(Class<?> className) {
		if (map.containsKey(className)) {
			return map.get(className);
		} else {
			return null;
		}
	}

	public static boolean deRegister(Class<?> className) {
		if (map.containsKey(className)) {
			map.remove(className);
			return true;
		} else {
			return false;
		}
	}

	public static void deRegisterAll() {
		map.clear();
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
	}
	
	public static void register(Object object){
		if(object != null ){
			register(object.getClass(), object);
		}
	}
}
