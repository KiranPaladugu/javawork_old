package com.tcs.tmp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.logService.Logger;
import com.tcs.berReader.gui.MessageHandler;

public class RunMe {
	private FileWriter writer;
	private String relativePath;
	private String classes;
	private String bin;
	private String root;
	private String lib;
	private String log;
	private String batchtext="";

	public static void main(String args[]) {
		RunMe run = new RunMe();
		run.build();
	}

	private void buildBatchFile(){
		File fi = new File("myname");
		relativePath = fi.getAbsolutePath();
		Logger.log((relativePath));
		int last = relativePath.lastIndexOf("\\");
		classes = relativePath.substring(0, last);
		last = classes.lastIndexOf("\\");
		root = relativePath.substring(0, last);

		bin = root + "\\bin";
		lib = root + "\\lib";
		log = root + "\\log";

		Logger.log("ROOT :" + root);
		Logger.log("bin :" + bin);
		Logger.log("lib :" + lib);
		Logger.log("log :" + log);

		batchtext += "echo off\n";
		batchtext += "cd ../classes/\n";
		batchtext += "SET CLASSPATH=%CLASSPATH%;.;";
		File file = new File(lib);
		if (file.exists() && file.isDirectory()) {
			String names[] = file.list();
			for (String name : names) {
				if (name.endsWith(".jar") || name.endsWith(".JAR")) {
					batchtext += lib + "\\" + name + ";";
				}
			}
			batchtext += "\n";
		}		
		batchtext += "java com.tcs.berReader.gui.GuiRunner " + log;
		batchtext += "\ncd ../bin/\n";
		batchtext += "\nIF EXIST .\\Run2.bat (\n" + "\tDEL /F .\\Run2.bat\n" + ")\n";
		
		//batchtext += "Delt.bat";
		File newFile = new File(bin + File.separator + "Run2.bat");
		Logger.log("BatchFile :" + newFile.getAbsolutePath());
		// Logger.log(batchtext);
		try {
			if (!newFile.exists()) {
				newFile.createNewFile();
			} else {
				boolean deleted = newFile.delete();
				if (deleted) {
					newFile.createNewFile();
				} else {
					MessageHandler.displayErrorMessage("You donot have enough permissions to excecute.");
				}
			}
			writer = new FileWriter(newFile);
			writer.write(batchtext);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			MessageHandler.displayErrorMessage("You donot have enough permissions to excecute.");
			e.printStackTrace();
		}
		Logger.closeLog();
	}
	
	private void build() {
		String os = System.getProperty("os.name");
		if (os.contains("Windows")) {
			buildBatchFile();
		}else if (os.contains("Unix")){
			buildShellScriptFile();
		}
	}

	private void buildShellScriptFile() {
		
	}
}
