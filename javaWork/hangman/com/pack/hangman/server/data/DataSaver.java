package com.pack.hangman.server.data;

import java.util.Date;
import java.util.Vector;

import com.pack.hangman.data.Data;
import com.pack.hangman.server.update.ui.WindowPanel;

public class DataSaver {
	DataBaseManager db;

	public DataSaver() {

	}

	public boolean save(String str[]) {
		db = new DataBaseManager();
		Data data = new Data();
		data.setCurrent(new Date());
		data.setLastUpdated(new Date());
		data.setUpdate(false);
		data.setVersion(WindowPanel.ver.getText());
		Vector<String> vector = new Vector<String>();
		for (int i = 0; i < str.length; i++) {
			vector.add(str[i]);
		}
		data.setData(vector);
		db.write(data);
		System.out.println("DataBase UPDATED..");
		db.closeWriter();
		return false;
	}
}
