package com.tcs.berReader.gui;


public class LocationInfo {
	private int rack;
	public int getRack() {
		return rack;
	}
	public void setRack(int rack) {
		this.rack = rack;
	}
	public int getShelf() {
		return shelf;
	}
	public void setShelf(int shelf) {
		this.shelf = shelf;
	}
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public int getCard() {
		return card;
	}
	public void setCard(int card) {
		this.card = card;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	private int shelf;	
	private int slot;
	private int card;
	private int port;
	
	@Override
	public boolean equals(Object obj) {
		LocationInfo loc = (LocationInfo)obj;
		if(loc.toString().equals(this.toString())){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		String str ="";
		str+="Shelf-"+shelf+"/Card-"+card+"/Port-"+port;
		return str;
	}
}
