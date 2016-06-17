package testClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class OutputStreamHandler implements Runnable {
	OutputStream out;
	Process process;
	public OutputStreamHandler(OutputStream out ,Process process) {
		this.process = process;
		this.out=out;
		Thread t=new Thread(this);
		t.start();
	}
	public void run() {		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg="";
		try {
			while((msg=br.readLine())!=null){				
				out.write(msg.getBytes());
				out.flush();
				System.out.println("flushing..");
				InputStream input = process.getInputStream();
				InputStremHandler inHandle = new InputStremHandler(input);
				inHandle.stop();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
}
