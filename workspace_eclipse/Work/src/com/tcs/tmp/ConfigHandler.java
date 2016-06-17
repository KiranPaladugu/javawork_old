package com.tcs.tmp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.framework.gui.view.AppConfig;
import com.logService.Logger;
import com.tcs.berReader.gui.ApplicationContext;
import com.tcs.berReader.gui.MessageHandler;

public class ConfigHandler {

	private static final String PASSWORD = "PASSWORD";
	private static final String SECRET_KEY = "DES";
	private static final String CIPHER_NAME = "DES/ECB/PKCS5Padding";
	private String defaultName = "App.config";

	public void saveConfiguration(AppConfig config) {
		try {
			byte[] key = PASSWORD.getBytes();
			DESKeySpec desKeySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			// Create Cipher
			Cipher desCipher = Cipher.getInstance(CIPHER_NAME);
			desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			File file = new File(ApplicationContext.getConf()
					+ File.separator
					+ ApplicationContext.getApplicationProfile().getProperty("BERViewer.application.file.name.configuration",
							defaultName));
			
			if(!file.exists()){	
				File parent = file.getParentFile();
				if(!parent.exists()){
					parent.mkdirs();
				}
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			CipherOutputStream cos = new CipherOutputStream(bos, desCipher);
			ObjectOutputStream oos = new ObjectOutputStream(cos);
			oos.writeObject(config);
			oos.flush();
			oos.close();
			Logger.log("Configuratin saved Successfull to : " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			MessageHandler.displayErrorMessage("Configuration is not saved!");
		}
	}

	public AppConfig loadConfig() {
		AppConfig config = new AppConfig();
		File file = new File(ApplicationContext.getConf()
				+ File.separator
				+ ApplicationContext.getApplicationProfile().getProperty("BERViewer.application.file.name.configuration",
						defaultName));
		try {
			byte[] key = PASSWORD.getBytes();
			DESKeySpec desKeySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			// Create Cipher
			Cipher desCipher = Cipher.getInstance(CIPHER_NAME);
			desCipher.init(Cipher.DECRYPT_MODE, secretKey);
			if (file != null && file.exists() && file.isFile()) {
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				CipherInputStream cis = new CipherInputStream(bis, desCipher);
				ObjectInputStream ois = new ObjectInputStream(cis);

				Object obj = ois.readObject();
				ois.close();

				config = (AppConfig) obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("Unable to read Configuration..: old configuration is not useful.. removing..");
			config = new AppConfig();
			if (file.delete()) {
				Logger.log("ConfigDeletion Success.");
			}
		}

		return config;
	}
}
