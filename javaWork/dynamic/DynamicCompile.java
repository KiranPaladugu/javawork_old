import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class DynamicCompile {
	Date today = new Date();
	String todayMillis = Long.toString(today.getTime());
	String todayClass = "z_" + todayMillis;
	String todaySource = todayClass + ".java";

	public static void main(String args[]) {
		DynamicCompile mtc = new DynamicCompile();
		mtc.createIt();
		if (mtc.compileIt()) {
			System.out.println("Running " + mtc.todayClass + ":\n\n");
			mtc.runIt();
		} else
			System.out.println(mtc.todaySource + " is bad.");
	}

	public void createIt() {
		try {
			FileWriter aWriter = new FileWriter(todaySource, true);
			aWriter.write("public class " + todayClass + "{");
			aWriter.write(" public void doit() {");
			aWriter.write(" System.out.println(\"" + todayMillis + "\");");
			aWriter.write(" }}\n");
			aWriter.flush();
			aWriter.close();
			System.out.println("Writing to "+todaySource +"\nDONE!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean compileIt() {
		System.out.println("Compiling started for ..:"+todaySource);
		String[] source = { new String(todaySource) };
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		new sun.tools.javac.Main(baos, source[0]).compile(source);
		// if using JDK >= 1.3 then use
		// public static int com.sun.tools.javac.Main.compile(source);		
		System.out.println("Compiling Done for ..:"+todaySource+"  :"+baos.toString());
		return (baos.toString().indexOf("error") == -1);
	}

	public void runIt() {
		try {
			Class params[] = {};
			Object paramsObj[] = {};
			Class thisClass = Class.forName(todayClass);
			Object iClass = thisClass.newInstance();
			Method thisMethod = thisClass.getDeclaredMethod("doit", params);
			thisMethod.invoke(iClass, paramsObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}