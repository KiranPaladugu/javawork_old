package com.tcs.berReader.gui;

import javax.swing.DefaultListModel;

public class BerFileListModel extends DefaultListModel {

	private static final long serialVersionUID = -8732959369996832545L;

	public void addElement(BerFileListItem item) {
		super.addElement(item);
	}
	
	public void add(BerFileListItem item){
		super.addElement(item);		
	}
	
	public BerFileListItem get(int index) {
		return (BerFileListItem)super.get(index);
	}
	
	public BerFileListItem getElementAt(int index) {
		return (BerFileListItem)super.getElementAt(index);
	}
	
	public boolean removeElement(BerFileListItem item) {
		
		return super.removeElement(item);
	}
}
