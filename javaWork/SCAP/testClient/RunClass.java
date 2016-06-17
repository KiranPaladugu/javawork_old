package testClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RunClass {
	public static void main(String[] args) throws IOException {
		Runtime run=Runtime.getRuntime();
		Process p = run.exec("cmd");
		InputStream input = p.getInputStream();
		InputStremHandler inHandle = new InputStremHandler(input);
		inHandle.stop();
		OutputStream output = p.getOutputStream();
		OutputStreamHandler outHandle = new OutputStreamHandler(output,p);
	}
}
