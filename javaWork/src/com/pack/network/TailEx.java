package com.pack.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;

public class TailEx {
	public static void main(String args[]) {
		int num = Integer.parseInt(args[0]);
		new TailEx(num);
	}
	RandomAccessFile raf;
	String fileName = "TMFAgent.log";
	File file;
	int lines = 0;
	int pos[];

	int number = 0;

	public TailEx() {
		file = new File(fileName);
		this.getLines(file);
		this.tailLinePosition(file, number);
	}

	public TailEx(int number) {
		file = new File(fileName);
		this.getLines(file);
		this.tailLinePosition(file, number);
	}

	public long getLines(File file) {
		FileReader fr = null;
		LineNumberReader lineReader;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to access to file");
			System.exit(0);
		}
		lineReader = new LineNumberReader(fr);
		String newLine = null;
		try {
			while ((newLine = lineReader.readLine()) != null) {

			}
			lines = lineReader.getLineNumber();
			System.out.println("Total lines:" + lineReader.getLineNumber());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return lines;
	}

	public void tailLinePosition(File file, int number) {
		FileReader fr = null;
		LineNumberReader lineReader;
		long atPostion = lines - number;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to access to file");
			System.exit(0);
		}
		lineReader = new LineNumberReader(fr);
		String newLine = null;
		try {
			while ((newLine = lineReader.readLine()) != null) {
				if (lineReader.getLineNumber() >= atPostion) {
					System.out.println(newLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
