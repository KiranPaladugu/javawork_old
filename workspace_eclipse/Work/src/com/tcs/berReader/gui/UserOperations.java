package com.tcs.berReader.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.framework.gui.view.AppConfig;
import com.framework.gui.view.ConfigValue;
import com.framework.reg.Register;
import com.framework.reg.RegisterException;
import com.logService.Logger;
import com.tcs.berReader.BERFileData;
import com.tcs.berReader.BerReader;
import com.tcs.berReader.BerReader.BerFileFilter;
import com.tcs.tmp.ConfigHandler;

public class UserOperations {
	private BerFileListHandler handler;
	private boolean isKeepOnLoad = true;
	private boolean isLoadOnDemand = false;
	private boolean isExitConfiramtionNeeded = true;

	public boolean isExitConfiramtionNeeded() {
		return isExitConfiramtionNeeded;
	}

	public boolean isKeepOnLoad() {
		return isKeepOnLoad;
	}

	public boolean isLoadOnDemand() {
		return isLoadOnDemand;
	}

	public UserOperations() {
		try {
			isKeepOnLoad = ApplicationContext.getApplicationProperties()
					.getBoolProperty("Application.BERViewer.KeepOnLoad", true);
			isLoadOnDemand = ApplicationContext.getApplicationProperties().getBoolProperty("Application.BERViewer.loadOnDemand",
					true);
			isExitConfiramtionNeeded = ApplicationContext.getApplicationProperties().getBoolProperty(
					"Application.Window.Exit.Confirmation", true);
			handler = (BerFileListHandler) Register.getCheckedObject(BerFileListHandler.class);
		} catch (RegisterException e) {
			e.printStackTrace();
		}
	}

	public void applicationExit(boolean ensure) {
		if (ensure && !ensureClose()) {
			return;
		}
		if (ApplicationContext.getApplicationProperties().getBoolProperty("Application.saveConfiguration", true)) {
			ConfigHandler handler = new ConfigHandler();
			handler.saveConfiguration(ApplicationContext.getApplicationConfiguration());
		} else {
			System.out.println("Saving Configuration Disabled!!");
		}
		exit();
	}

	private boolean ensureClose() {
		if (!isExitConfiramtionNeeded) {
			return true;
		} else {
			int selection = JOptionPane.showConfirmDialog(getFrame(), "Do you Want to exit BERViewr ?\nAre u sure ?",
					"Confirmation!", JOptionPane.YES_NO_OPTION);
			if (selection == JOptionPane.OK_OPTION) {
				return true;
			}
		}
		return false;
	}

	public void openFileOperation() {
		ApplicationContext.setLoading(true);
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		final BerFileFilter logFilter = new BerReader().new BerFileFilter();
		fileChooser.setFileFilter(logFilter);
		final int x = fileChooser.showDialog(null, "Select File(s) to load..");
		if (x == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles();
			for (File file : files) {
				identifyBERs(file, false);
			}
		}
		ApplicationContext.setLoading(false);
	}

	/**
	 * @param file
	 */
	private void openBERFile(File file) {
		BERFileData data = null;
		if (!isLoadOnDemand()) {
			data = handler.getBERData(file);
		}
		BerFileListItem item = new BerFileListItem(file, data);
		getBERVersion(file);
		addToBERList(item);
	}

	public void openFolderOperation() {
		ApplicationContext.setLoading(true);
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final int x = fileChooser.showDialog(null, "Select Folder to load..");
		File file = null;
		if (x == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			identifyBERs(file, true);
		}
		ApplicationContext.setLoading(false);
	}

	private void identifyBERs(File file, boolean flag) {
		if (file.isDirectory()) {
			String parent = file.getParent();
			String[] files = file.list();
			for (String name : files) {
				File f = new File(parent + File.separator + file.getName() + File.separator + name);
				if (file.isDirectory()) {
					if (flag) {
						identifyBERs(f, flag);
					}
				} else {
					verifyAndLoadBER(file);
				}
			}
		} else {
			verifyAndLoadBER(file);
		}
	}

	private void verifyAndLoadBER(File file) {
		if (file != null) {
			if (file.getName().endsWith(".ber") || file.getName().endsWith(".BER") || file.getName().endsWith("ber.realigned")
					|| file.getName().endsWith("ber.realigned".toUpperCase())) {
				openBERFile(file);
			}
		}
	}

	private void addToBERList(BerFileListItem item) {
		BERListPane list = (BERListPane) Register.getObject(BERListPane.class);
		if (list != null) {
			list.addToList(item);
		}
	}

	public void saveFileOperation() {
		operationNotYetSupported();
	}

	public void saveAsFileOperation() {
		operationNotYetSupported();
	}

	private void exit() {
		Logger.closeLog();
		System.exit(0);
	}

	private void operationNotYetSupported() {
		MessageHandler.displayInfoMessage("Operation is not yet Supoorted!.. Try agin..");
	}

	public void loadOpertation() {
		operationNotYetSupported();

	}

	public void showHelpContentsOpertaion() {
		operationNotYetSupported();

	}

	public String getBERVersion(File file) {
		boolean flag=false;
		if(!ApplicationContext.isLoading()){
			flag = true;
			ApplicationContext.setLoading(true);
		}
		String name = file.getName();
		String version = null;
		int major = 0;
		int sub = 0;
		int minor = 0;
		try {
			int ind = name.lastIndexOf(".ber");
			if (ind != -1) {
				name = name.substring(0, ind);
				// removed extention.
				ind = name.lastIndexOf('_');
				try {
					if (ind != -1) {
						minor = Integer.parseInt(name.substring(ind + 1));
						name = name.substring(0, ind);
						ind = name.lastIndexOf('_');
						if (ind != -1) {
							sub = Integer.parseInt(name.substring(ind + 1));
							name = name.substring(0, ind);
							ind = name.lastIndexOf('_');
							if (ind != -1) {
								major = Integer.parseInt(name.substring(ind + 1));
							}
						}
					}
					if (major != 0 && minor != 0 && sub != 0) {
						version = major + "." + sub + "." + minor;
					}
				} catch (Exception e) {
					major = 0;
					sub = 0;
					minor = 0;
					version = null;
				}
			}
		} catch (Throwable t) {
			version = null;
			System.out.println("Unable to find Version for" + file.getAbsolutePath());
		}
		if (version != null) {
			System.out.println("BER file Version is :" + version);
		} else {
			System.out.println("Unable to identify BERFile Version :(");
		}
		if(flag){
			ApplicationContext.setLoading(false);
		}
		return version;
	}

	public void writeToTextFile(BERFileData data, String path) {
		operationNotYetSupported();
	}

	public boolean writeToTextFile(BerFileListItem data, String path) {
		boolean flag1=false;
		if(!ApplicationContext.isLoading()){
			flag1 = true;
			ApplicationContext.setLoading(true);
		}
		File file = new File(path);
		boolean flag = true;
		if (file.exists()) {
			file.delete();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				MessageHandler.displayErrorMessage("File creationg Failed for :" + file.getAbsolutePath());
			}
		}
		try {
			FileWriter writer = new FileWriter(file);
			if (data.getValue() != null) {
				writer.write(data.getValue().toString());
			} else if (isLoadOnDemand() && !isKeepOnLoad()) {
				MessageHandler.displayErrorMessage("Operation is not Supported in the mode you are running.");
				flag = false;
			}
			writer.flush();
			writer.close();
			if (flag) {
				MessageHandler.displayInfoMessage("Writing ..\n SUCESS for  :" + data.getKey().getAbsolutePath() + "|n @\n "
						+ file.getAbsolutePath());
			}
		} catch (IOException e) {
			MessageHandler.displayErrorMessage("Unexpected Error while writing file  :" + data.getKey().getAbsolutePath());
		}
		if(flag1){
			ApplicationContext.setLoading(false);
		}
		return flag;
	}

	public void showHelpAbout() {
		DisplayDialog.displayCreditsMessage();
	}

	public void showPreferencesOperation() {
		operationNotYetSupported();
	}

	public void loadLicenseOperation() {
		boolean flag = updateLicense();
		if (flag) {
			MessageHandler.displayInfoMessage("License updated ! Please restart the application.");
		} else {
			MessageHandler.displayErrorMessage("License ERROR !! \n please try again.");
		}
	}

	public JFrame getFrame() {
		JFrame frame = null;
		Object obj = Register.getObject(UserInterface.class);
		if (obj != null) {
			frame = ((UserInterface) obj).getFrame();
		}
		return frame;
	}
	
	public void updateLicenseOperation(){
		String msg = "License will be updated, Previous license will be removed \n Are you sure to update License this ?\n\nNOTE : You will reicieve notification if License is update is success!";
		int x = JOptionPane.showConfirmDialog(getFrame(), msg, "License Update Confirmation!", JOptionPane.YES_NO_OPTION);
		if (x == JOptionPane.YES_OPTION) {
			boolean flag = updateLicense();
			if (flag) {
				MessageHandler.displayInfoMessage("License updated SuceessFully! .");
			} else {
				MessageHandler.displayErrorMessage("License ERROR !! \n please try again.");
			}
		}else{
			MessageHandler.displayInfoMessage("License update aborted! .");
		}
	}

	private boolean updateLicense() {
		boolean flag = false;
		try {
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(false);
			final int x = fileChooser.showDialog(null, "Select License File to update..");
			if (x == JFileChooser.APPROVE_OPTION) {

				File srFile = fileChooser.getSelectedFile();
				File dtFile = new File(ApplicationContext.getConf()
						+ File.separator
						+ ApplicationContext.getApplicationProfile().getProperty("BERViewer.application.file.name.license",
								"License.lic"));
				ApplicationContext.getConf();

				if (srFile.isFile()) {
					InputStream in = new FileInputStream(srFile);
					OutputStream out = new FileOutputStream(dtFile);
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
					AppConfig config = ApplicationContext.getApplicationConfiguration();
					File file = dtFile.getAbsoluteFile();
					ConfigValue value = new ConfigValue(file);
					config.putConfig("License.location", value);
					flag = true;
				}
			}
		} catch (Exception e) {
			flag = false;
		} catch (Throwable t) {
			flag = false;
		}
		return flag;
	}

	public void berMakerOperation() {
		operationNotYetSupported();
	}

}
