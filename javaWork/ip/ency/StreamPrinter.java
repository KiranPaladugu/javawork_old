package ency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StreamPrinter {
	private File file;
	private int count;

	public StreamPrinter() {

	}

	public StreamPrinter(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void print() {
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			File f = new File("new.jpg");
			FileOutputStream fos = new FileOutputStream(f);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int c = -1;
			while ((c = bis.read()) != -1) {
				count++;
				bos.write(c);				
			}
			bos.close();
			fos.close();
			bis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		File f = new File("Sunset.jpg");
		new StreamPrinter(f).print();
	}
}
