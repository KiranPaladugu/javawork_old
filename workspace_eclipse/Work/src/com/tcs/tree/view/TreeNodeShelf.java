package com.tcs.tree.view;

import com.marconi.fusion.X36.X36ShelfInformation;
import com.marconi.fusion.base.asn1.ASN1Exception;

public class TreeNodeShelf extends AbstractSetTypeUserTreeNode{

	private X36ShelfInformation shelf;
	
	public TreeNodeShelf(X36ShelfInformation shelf) {
		super(shelf);
		this.shelf = shelf;
	}
	
	@Override
	public String getSyntax() {
		if(shelf != null)
		return shelf.toString();
		else
			return "null";
	}

	@Override
	public String getNormalizedData() {		
		try {
			return shelf.format();
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getName() {
		if(shelf !=null && shelf.getShelfId() != null)
		return "Shelf:"+shelf.getShelfId().getValue();
		else
			return "<UN-DEFINED>";
	}

	@Override
	public int getId() {
		if(shelf!=null && shelf.getShelfId()!=null)
		return shelf.getShelfId().getValue();
		else return 0;
	}

	@Override
	public String getSyntaxName() {
		if(shelf!=null)
		return shelf.getClass().getSimpleName();
		else return "<UN-DEFINED>";
	}
	

	public String toString() {
		return getName();
	}

	@Override
	public String getSpecificInfo() {
		if(shelf!=null && shelf.getShelfId()!=null)
		return shelf.getShelfType().toString();
		else return NULL;
	}
}
