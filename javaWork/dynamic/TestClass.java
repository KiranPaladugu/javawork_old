import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestClass {
	public static void main(String args[]) throws Throwable {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter class Name to read Method..");
		String className = br.readLine();
		TestClass test = new TestClass();
		Object classObj = null;
		try {
			classObj = test.getInstace(className);
			test.displayAllmethods(classObj);
			System.err.println("Enter method name to invoke.");
			String methodName = br.readLine();
			List<Method> list = test.getMethodsWithName(methodName, classObj);
			if (list.size() == 0) {
				System.out.println("No such method Exists.");
			} else if (list.size() == 1) {
				System.out.println("Invoking started..");
				int nargs =test.getNumberOfArgs(list, methodName, 0);
				System.out.println("Number of arguments:"+nargs);
				Class[] parms = test.getParmeterTypes(list.get(0));
				if(parms.length==0){
					Object out=list.get(0).invoke(classObj, null);
					System.out.println("Method returned:"+out);
				}else {
					
				}				
			} else {
				System.out.println("Select one from above.");
				int nargs =test.getNumberOfArgs(list, methodName, 0);
				System.out.println("Number of arguments:"+nargs);
				Class[] parms = test.getParmeterTypes(list.get(0));
				if(parms.length==0){
					Object out=list.get(0).invoke(classObj, null);
					System.out.println("Method returned:"+out);
				}else{
					test.evalutateInputParams(parms);
				}
			}
		} catch (InstantiationException ie) {
			System.out
					.println("Looks like this is Interface or Abstact Class.\nOr This class may have Constructor with parmeterized\nPlease Enter Class Name:");
		} catch (IllegalAccessException ie) {
			System.out.println("Don't Make me laugh...\n You dont have access to this ...");
		}
	}

	public Object getInstace(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ClassLoader loader = TestClass.class.getClassLoader();
		Object classObj = loader.loadClass(className).newInstance();
		return classObj;
	}

	public Method[] getAllmethods(Object classObj) {
		return classObj.getClass().getDeclaredMethods();
	}

	public Method[] displayAllmethods(Object classObj) {
		Method[] methods = classObj.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			System.out.println(methods[i]);
		}
		return methods;
	}

	public List<Method> getMethodsWithName(String name, Object classObj) {
		List<Method> list = new ArrayList<Method>();
		Method[] methods = getAllmethods(classObj);
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(name)) {
				list.add(methods[i]);
				System.out.println(list.size() + "." + methods[i]);
			}
		}
		return list;
	}

	public int getNumberOfArgs(List<Method> list, String methodName, int index) {
		int x = -1;
		if (list.get(index).getName().equals(methodName)) {
			Class<?>[] p = list.get(index).getParameterTypes();
			x = p.length;
		}
		return x;
	}
	public Class<?>[] getParmeterTypes(Method method){
		return method.getParameterTypes();
	}
	public void evalutateInputParams(Class[] params){
		for(Class param:params){
			System.out.println(param.getName());
		}
	}
}
