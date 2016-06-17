package com.pack.hangman.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.pack.hangman.err.HangedException;

public class DataManager {
	private Vector data;
	ObjectInputStream ois;
	ObjectOutputStream ous;
	FileInputStream fis;
	FileOutputStream fos;
	RandomAccessFile raf;
	String fileName = "hangmandata.db";
	File file, readFile;
	String path;

	public DataManager() throws HangedException {
		CodeSource src = DataManager.class.getProtectionDomain().getCodeSource();
		if (src != null) {
			URL url = null;
			try {
				url = new URL(src.getLocation(), fileName);
			} catch (MalformedURLException e) {
				JOptionPane
						.showMessageDialog(
								null,
								"\nUnable to get Data.\nNow Exiting ...If the problem persists please install the application again.\nSorry for the incovinience .",
								"Data not found", JOptionPane.ERROR_MESSAGE);
			}
			path = url.getPath();
		}
		file = new File(fileName);
		readFile = new File(fileName);
		System.out.println(fileName);
		try {
			if (readFile.exists()) {
				fis = new FileInputStream(readFile);
				System.out.println("Available of stream" + fis.available());
				ois = new ObjectInputStream(fis);
			} else
				throw new Exception("DATA NOT FOUND!!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"\nUnable to get Data.\nPlease restart the application. data Creating...\nNow Exiting ...", "Data not found",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private void createData() {

	}
}
