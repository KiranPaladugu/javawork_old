package com.pack.hangman.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String version;
	public int higherVer;
	public int lowerVer;
	public int lower1Ver;
	public Date current;
	public Date lastUpdated;
	public boolean isUpdate;
	public boolean isUpdateAvailable;

	public Vector<String> data;

	public Data() {

	}

	public Date getCurrent() {
		return current;
	}

	public Vector getData() {
		return data;
	}

	public int getHigherVer() {
		return higherVer;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public int getLower1Ver() {
		return lower1Ver;
	}

	public int getLowerVer() {
		return lowerVer;
	}

	public String getVersion() {
		return version;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public boolean isUpdateAvailable() {
		return isUpdateAvailable;
	}

	public void setCurrent(Date current) {
		this.current = current;
	}

	public void setData(Vector data) {
		this.data = data;
	}

	public void setHigherVer(int higherVer) {
		this.higherVer = higherVer;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setLower1Ver(int lower1Ver) {
		this.lower1Ver = lower1Ver;
	}

	public void setLowerVer(int lowerVer) {
		this.lowerVer = lowerVer;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public void setUpdateAvailable(boolean isUpdateAvailable) {
		this.isUpdateAvailable = isUpdateAvailable;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
