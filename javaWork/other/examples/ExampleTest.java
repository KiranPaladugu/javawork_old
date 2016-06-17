package examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExampleTest {
	public static void main(String args[]) {
		new ExampleTest().process("notepad");
	}
	String command = null;
	InputStream in;

	OutputStream out;

	ExampleTest() {
	}

	/**
	 * @param cmd
	 */
	public void process(String cmd) {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec(cmd);
			in = process.getInputStream();
			out = process.getOutputStream();
			int counter = 0;
			int c = -1;
			while ((c = in.read()) != -1) {
				System.out.print((char) c);
				// System.out.print(" - ");
				// System.out.println(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
