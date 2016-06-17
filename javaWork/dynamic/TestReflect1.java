

import java.lang.reflect.Method;

public class TestReflect1 {
  static void invoke(String aClass, String aMethod, Class[] params, Object[] args) {
    try {
      Class c = Class.forName(aClass);
      Method m = c.getDeclaredMethod(aMethod, params);      
      Object i = c.newInstance();
      Object r = m.invoke(i, args);
      } 
    catch (Exception e) {
      e.printStackTrace();
      } 
    }

  public static void main(String[] args) {
    invoke("Class1", "say", new Class[] {String.class, String.class}, 
           new Object[] 
             {new String("Hello"), new String("World")});
    }
}