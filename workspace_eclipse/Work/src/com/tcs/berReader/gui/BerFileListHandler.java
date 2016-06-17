package com.tcs.berReader.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.logService.Logger;
import com.tcs.berReader.BERFileData;
import com.tcs.berReader.BerReader;

public class BerFileListHandler {
	BerReader reader = new BerReader();
	Map<File, BERFileData> map = new HashMap<File, BERFileData>();
	private boolean isKeepOnDemand = true;

	public BerFileListHandler() {
		isKeepOnDemand =  ApplicationContext.getApplicationProperties().getBoolProperty("Application.BERViewer.KeepOnLoad", true);
		reader.setAutoWrite(false);
	}

	public BERFileData getBERData(File file) {
		BERFileData data = null;
		if (isKeepOnLoad()) {
			if (map.containsKey(file)) {
				data = map.get(file);
			} else {
				data = loadBERData(file);
				if (data == null) {
					MessageHandler.displayErrorMessage("Unable to read BerFile!!..");
				}
			}
		} else {
			data = loadBERData(file);
			if (data == null) {
				MessageHandler.displayErrorMessage("Unable to read BerFile!!..");
			}
		}
		return data;

	}

	@SuppressWarnings("unused")
	private void dumpInfo() {
		Set<File> keySet = map.keySet();
		Iterator<File> itr = keySet.iterator();
		StringBuffer buf = new StringBuffer("DATA  START:{\n");
		while (itr.hasNext()) {
			File f = itr.next();
			BERFileData obj = map.get(f);
			buf.append(f.getName());
			buf.append(":");
			buf.append(obj.hashCode());
			buf.append(",\n");
		}
		buf.append("} END");
		System.out.println(buf.toString());
	}

	private BERFileData loadBERData(File file) {
		BERFileData data = null;
		try {
			data = reader.read(file);
			map.put(file, data);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.log(e.getLocalizedMessage());
			Logger.log(e.getMessage());
		}
		return data;
	}

	public boolean remove(File file) {
		return (map.remove(file) != null);
	}

	private boolean isKeepOnLoad() {
		return isKeepOnDemand;
	}
}
