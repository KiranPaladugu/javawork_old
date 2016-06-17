package com.pack.hangman.data;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import com.pack.hangman.log.Logger;

public class DataBaseManager {
	File file, readingFile;
	String fileName = "hangmandata.db";
	ObjectOutputStream oos;
	FileOutputStream fos;
	FileInputStream fis;
	ObjectInputStream ois;
	Data data;

	public DataBaseManager() {
	}

	public void closeReader() {
		try {
			this.ois.close();
			this.fis.close();
			ois = null;
			fis = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeWriter() {
		try {
			this.oos.close();
			this.fos.close();
			oos = null;
			fos = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initReader() {
		// file=new File(fileName);
		readingFile = new File(fileName);
		if (!readingFile.exists()) {
			Logger.log("Unable to create");
			JOptionPane.showMessageDialog(null, "Unable to get Data \n now exiting..", "ERROR!!", JOptionPane.ERROR_MESSAGE);
			Logger.closeLog();
			System.exit(0);
		}

	}

	private void initWriter() {
		file = new File(fileName);
		if (!file.exists()) {
			Logger.log("creating DataBase.....");
			try {
				file.createNewFile();
			} catch (IOException e) {
				Logger.log("Unable to create");
				JOptionPane.showMessageDialog(null, "Unable to create DataBase \n now exiting..", "ERROR!!",
						JOptionPane.ERROR_MESSAGE);
				Logger.closeLog();
				System.exit(0);
				e.printStackTrace();
			}
		}

	}

	public Data readData() {
		if (data != null) {
			return data;
		}
		initReader();
		try {
			fis = new FileInputStream(readingFile);
			ois = new ObjectInputStream(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// ois.reset();
			if (readingFile.exists()) {
				Object obj;
				while ((obj = ois.readObject()) != null) {
					if (obj instanceof Data) {
						data = (Data) obj;
						break;
					}
				}
				return data;
			}
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			/*
			 * } catch (ClassNotFoundException e) { // TODO Auto-generated catch
			 * block e.printStackTrace();
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public boolean write(Data data) {
		initWriter();
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (oos != null) {
			try {
				oos.writeObject(data);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
