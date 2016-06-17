package com.pack.hangman.server.data;

import java.util.Vector;

import com.pack.hangman.data.Data;

public class DataReader {
	DataBaseManager db;

	public DataReader() {

	}

	public String[] getData() {
		String[] str = null;
		db = new DataBaseManager();
		Data data = db.readData();
		Vector<String> vector = data.getData();
		str = new String[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			str[i] = vector.get(i);
		}
		db.closeReader();
		return str;
	}

	public String getVersion() {
		db = new DataBaseManager();
		Data data = db.readData();
		return data.getVersion();
	}

}
