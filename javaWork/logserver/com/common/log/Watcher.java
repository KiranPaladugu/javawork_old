package com.common.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class Watcher {
	File file;
	String fileName = "Logger.log";
	FileWriter writer;
	BufferedWriter bwriter;
	public boolean isDisabled;

	public Watcher() {
		file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Unable to create Log file..\nYour Problems were not logged.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
				isDisabled = true;
			}
		}
		try {
			if (!isDisabled)
				writer = new FileWriter(file, true);
			bwriter = new BufferedWriter(writer);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to get Channel to Write to Log file..\nYour Problems were not logged.",
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
			isDisabled = true;
		}
	}

	public void log(String msg) {
		if (!isDisabled) {
			if (writer != null) {
				try {
					bwriter.append(msg);
					bwriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
