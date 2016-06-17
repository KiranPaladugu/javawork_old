package testClient;

import java.io.IOException;
import java.io.InputStream;

public class InputStremHandler implements Runnable {
	private InputStream in;
	private volatile boolean  stop = false;
	public InputStremHandler(InputStream in) {
		this.in=in;
		Thread t=new Thread(this);
		t.start();
	}
	public void run() {		
		int c=-1;
		try {
			while ((c=in.read())!=-1){
				while(!stop)
				System.out.print((char)c);
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	public void stop(){
		stop = true;
	}
}
