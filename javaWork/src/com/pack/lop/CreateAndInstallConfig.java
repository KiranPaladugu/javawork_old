package com.pack.lop;

import java.io.File;
import java.io.FileWriter;

public class CreateAndInstallConfig {
	private final String comment = "#########################################################\n"
			+ "#             CONFIGURATION FILE                        #\n"
			+ "#########################################################\n";
	private final String language = "loc.language=english\n";
	private final String language_comment = "#language of chat optional . by default english.\n";
	private final String timeout = "loc.timeout=100000\n";
	private final String timeout_comment = "#time out ...\n";
	private final String defaultFolder = "loc.defaultFolder=%userprofile%\n";
	private final String defaultFolder_comment = "#default folder look up\n";
	private final String chunk_comment = "#Size of chunk(bandwith to use only for LAN)512Kb";
	private final String chunkSize = "loc.chunk.size=524288";

	private final String fileName = "config.properties";
	private final String path = "";
	private File file;

	public CreateAndInstallConfig() throws Exception {
		file = new File(path + fileName);
		this.createFile();
		this.writeNewConfig();
	}

	private boolean createFile() throws Exception {
		if (!file.exists()) {
			file.createNewFile();
			System.out.println("File created at" + file.getAbsolutePath());
			return true;
		}
		return false;
	}

	private boolean writeNewConfig() throws Exception {
		FileWriter writer = new FileWriter(file);
		writer.write(this.comment);
		writer.write(this.defaultFolder_comment);
		writer.write(this.defaultFolder);
		writer.write(this.timeout_comment);
		writer.write(this.timeout);
		writer.write(this.language_comment);
		writer.write(this.language);
		writer.write(this.chunk_comment);
		writer.write(chunkSize);
		writer.flush();
		writer.close();
		return false;
		
	}
}
