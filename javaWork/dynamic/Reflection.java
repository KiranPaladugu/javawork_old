

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
	public static void main(String[] args) {
		System.out.println(Reflection.class.getCanonicalName());
		try {
			Reflection sm = Reflection.class.newInstance();
			Class[] params = new Class[1];
			params[0]=Integer.TYPE;
			Object obargs[] = new Object[1];
			obargs[0]= new Integer(90);
			Method mt = Reflection.class.getDeclaredMethod("printName", params);
			Object ob=mt.invoke(sm,obargs);
			System.out.println("Method returned value:"+(Integer)ob);
			Method m=Reflection.class.getDeclaredMethod("printName", new Class[]{String.class});
			obargs[0]=new String("kiran");
			System.out.println("Method returned value:"+m.invoke(sm, obargs));
		} catch (InstantiationException e) {			
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (SecurityException e) {			
			e.printStackTrace();
		} catch (NoSuchMethodException e) {			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public int printName(int x){
		System.out.println("From printName():");
		System.out.println("Got Args AS:"+x);
		System.out.println("Exiting from printname();");
		return (x*x);
	}
	public String printName(String str){
		System.out.println("Hello :"+str);
		System.out.println("This is printName()... Thank you!!");
		return str;
	}
}
