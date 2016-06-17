package com.pack.hangman.utils;

import java.util.Date;

import javax.swing.JOptionPane;

import com.pack.hangman.data.Data;
import com.pack.hangman.data.DataBaseManager;
import com.pack.hangman.log.Logger;

public class UpdateChecker {
	Connector con;
	Data data, dat;

	public UpdateChecker(Data data) {
		this.data = data;
		con = new Connector("172.16.49.86", 7337);
	}

	public boolean check(int type) {
		dat = con.getUpdate(type);
		Date d1 = null, d2 = null;
		d1 = data.getCurrent();
		d2 = dat.getCurrent();
		if (d1.after(d2)) {
			data.setUpdateAvailable(true);
			return true;
		} else {
			data.setUpdateAvailable(false);
			return false;
		}
	}

	public boolean checkAll() {
		return false;
	}

	public int update() {
		int num = 0;
		if (check(Connector.DATA_UPDATE)) {
			Logger.log("Data Update Available..");
			if (dat != null) {
				DataBaseManager dbman = new DataBaseManager();
				if (dbman.write(dat))
					num++;
				dbman.closeWriter();
				Logger.log("Data Definations Updated.....");
			}
		} else {
			Logger.log("NO UPDATE AVAILABLE...");
		}
		return num;
	}

	public void updatePrograme() {
		if (check(Connector.PROGRAM_UPDATE)) {
			JOptionPane.showMessageDialog(null, "Programe Update Available", "Update!!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
