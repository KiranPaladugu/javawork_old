package com.framework.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.framework.gui.view.ConfigValue;
import com.tcs.berReader.gui.ApplicationContext;

public class LicenseReader {
	protected static final String PASSWORD = "CodeNamePassme";
	protected static final String SECRET_KEY = "DES";
	protected static final String CIPHER_NAME = "DES/ECB/PKCS5Padding";

	public License readLicense(String path) {
		File file = null;
		if (path == null || path.trim().equals("")) {
			path = ApplicationContext.getConf() + File.separator
					+ ApplicationContext.getApplicationProfile().getProperty("BERViewer.application.file.name.license", "License.lic");;

		}
		License license = null;
		String footPrint = FootPrint.getFootPrint();
		byte[] key = PASSWORD.getBytes();

		try {
			DESKeySpec desKeySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			// Create Cipher
			Cipher desCipher = Cipher.getInstance(CIPHER_NAME);
			desCipher.init(Cipher.DECRYPT_MODE, secretKey);
			Object object = ApplicationContext.getApplicationConfiguration().getObject("License.location");
			if (object == null) {
				return null;
			} else {
				ConfigValue value = (ConfigValue) object;
				file = (File) value.getObject();
			}
			if (file == null) {
				new File(path + "License_NEW_" + footPrint + ".license");
			}
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			CipherInputStream cis = new CipherInputStream(bis, desCipher);
			ObjectInputStream ois = new ObjectInputStream(cis);

			Object obj = ois.readObject();
			ois.close();

			license = (License) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return license;
	}

	public static void main(String[] args) {
		// new LicenseReader().generate("nothing","Kartheek Palla",License.EVOLUTION_TYPE);
		new LicenseReader().readLicense(null);
	}
}
