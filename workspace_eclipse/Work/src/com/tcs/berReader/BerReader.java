package com.tcs.berReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import com.logService.Logger;
import com.marconi.fusion.X36.X36MessageFactory;
import com.marconi.fusion.X36.X36MsgGetReportBshrProtection;
import com.marconi.fusion.X36.X36MsgGetReportNodeConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportNodeCrossConnections;
import com.marconi.fusion.X36.X36MsgGetReportNodePossibleConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportProtectionConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportProvisioning;
import com.marconi.fusion.X36.X36MsgReportGetCollection;
import com.marconi.fusion.X36.X36MsgReportGetDataProfiles;
import com.marconi.fusion.base.application.io.DecodecException;
import com.marconi.fusion.base.asn1.msg.FileMsgBerReader;
import com.marconi.fusion.base.asn1.msg.Message;
import com.marconi.fusion.base.asn1.msg.MessageFactory;

public class BerReader {
	private File file;
	private boolean autoWrite = true;

	public boolean isAutoWrite() {
		return autoWrite;
	}

	public void setAutoWrite(final boolean autoWrite) {
		this.autoWrite = autoWrite;
	}

	public class BerFileFilter extends FileFilter {
		@Override
		public boolean accept(final File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".ber")|| f.getName().toLowerCase().endsWith("ber.realigned");
		}

		@Override
		public String getDescription() {
			return ".ber";
		}
	}

	public BERFileData load() {
		BERFileData data = null;
		try {
			final String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			try {
				UIManager.setLookAndFeel(windowsTheme);
			} catch (final Exception e) {
				e.printStackTrace();
			}
			final JFileChooser fileChooser = new JFileChooser();
			final BerFileFilter logFilter = new BerReader().new BerFileFilter();
			fileChooser.setFileFilter(logFilter);
			final int x = fileChooser.showDialog(null, "Open");
			if (x == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				data=read(file);
			}			
		} catch (final Exception e) {
			e.printStackTrace();
			Logger.log(e.getMessage());
		}
		return data;
	}
	
	
	public File getFile(){
		return file;
	}

	public BERFileData read(final File file) throws IOException, DecodecException {
		BERFileData data = new BERFileData();
		final MessageFactory<Message<?>> msgfactory = new X36MessageFactory();
		final FileMsgBerReader<Message<?>> readber = new FileMsgBerReader<Message<?>>(file, msgfactory);
		Message<?> message = null;
		final String parent = getParentDirectory(file);
		final File newFile = getFiletoWrite(parent, file.getName(), 0);
		BufferedWriter writer = null;
		if (autoWrite) {
			writer = new BufferedWriter(new FileWriter(newFile, true));
		}
		Logger.log("Reading File :"+file.getName());
		while ((message = readber.readBER()) != null) {
			if (autoWrite) {
				writer.write(message.toString());
			}
			identify(message,data);
		}
		if (autoWrite) {
			writer.flush();
			writer.close();
			Logger.log("(-:-) written to FILE @ :" + newFile.getAbsolutePath());
		}
		return data;
	}

	private void identify(final Message<?> message, BERFileData data) {		
		if (message.getName().equalsIgnoreCase(BERFileData.X36MsgGetReportNodeConfiguration)) {
			final X36MsgGetReportNodeConfiguration nodeConfig = (X36MsgGetReportNodeConfiguration) message;
			data.setNodeConfig(nodeConfig);
			Logger.log(":):) NodeConfiguration Identified..");
		}  else if (message.getName().equalsIgnoreCase(BERFileData.X36MsgGetReportNodePossibleConfiguration)) {
			final X36MsgGetReportNodePossibleConfiguration nodePossibleConfig = (X36MsgGetReportNodePossibleConfiguration) message;
			data.setNodePossibleConfig(nodePossibleConfig);
			Logger.log(":):) DataProfiles Identified..");
		}else if (message.getName().equalsIgnoreCase(BERFileData.X36MsgReportGetDataProfiles)) {
			final X36MsgReportGetDataProfiles dataProfiles = (X36MsgReportGetDataProfiles) message;
			data.setDataProfiles(dataProfiles);
			Logger.log(":):) DataProfiles Identified..");
		} else if (message.getName().equalsIgnoreCase(BERFileData.X36MsgGetReportNodeCrossConnections)) {
			final X36MsgGetReportNodeCrossConnections nodeCrossConnections = (X36MsgGetReportNodeCrossConnections) message;
			data.setNodeCrossConnections(nodeCrossConnections);
			Logger.log(":):) NodeCrossConnections Identified..");
		} else if (message.getName().equalsIgnoreCase(BERFileData.X36MsgGetReportProtectionConfiguration)) {
			final X36MsgGetReportProtectionConfiguration protectionConfig = (X36MsgGetReportProtectionConfiguration) message;
			data.setProtectionConfig(protectionConfig);
			Logger.log(":):) NodeProtectionConfiguration Identified..");
		} else if (message.getName().equalsIgnoreCase(BERFileData.X36MsgReportGetCollection)) {
			final X36MsgReportGetCollection collection = (X36MsgReportGetCollection) message;
			data.setCollection(collection);
			Logger.log(":):) Collection Identified..");
		} else if (message.getName().equalsIgnoreCase(BERFileData.X36MsgGetReportBshrProtection)) {
			final X36MsgGetReportBshrProtection bshrProtection = (X36MsgGetReportBshrProtection) message;
			data.setBshrProtection(bshrProtection);
			Logger.log(":):) NodeBshrProtections Identified..");
		} else if (message.getName().equalsIgnoreCase(BERFileData.X36MsgGetReportProvisioning)) {
			final X36MsgGetReportProvisioning provisioning = (X36MsgGetReportProvisioning) message;
			data.setProvisioning(provisioning);
			Logger.log(":):) NodeAlarms Identified..");
		} else {
			Logger.log(" > > > Unidentified Message.....");
			Logger.log(" :) Messgae Family :" + message.getFamily());
			Logger.log(" :) Messgae type :" + message.getType());
			Logger.log(" :) Message Name :" + message.getName());
		}
	}


	private String getParentDirectory(final File file) {
		final File parent = file.getParentFile();
		if (parent.isDirectory()) {
			return parent.getAbsolutePath();
		} else {
			return getParentDirectory(parent);
		}
	}

	private File getFiletoWrite(final String path, final String nameSuggest, int delim) {
		String fileName = null;
		if (delim == 0) {
			fileName = path + File.separator + nameSuggest + ".txt";
		} else {
			fileName = path + File.separator + nameSuggest + "_" + delim + ".txt";
		}
		final File file = new File(fileName);
		if (file.exists()) {
			delim++;
			return getFiletoWrite(path, nameSuggest, delim);
		} else {
			return file;
		}
	}
}
