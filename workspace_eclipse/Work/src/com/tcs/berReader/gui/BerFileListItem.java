package com.tcs.berReader.gui;

import java.io.File;
import java.io.Serializable;

import com.tcs.berReader.BERFileData;

public class BerFileListItem implements Serializable{

	private static final long serialVersionUID = -7798625510154762717L;
	private File name;
	private BERFileData value;
	public BerFileListItem(File name, BERFileData value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name.getName();
	}
	
	public File getKey(){
		return name;
	}
	
	public void setName(File name) {
		this.name = name;
	}
	public BERFileData getValue() {
		return value;
	}
	public void setValue(BERFileData value) {
		this.value = value;
	}
	
	public String toString(){
		return name.getName();
		
	}
}
