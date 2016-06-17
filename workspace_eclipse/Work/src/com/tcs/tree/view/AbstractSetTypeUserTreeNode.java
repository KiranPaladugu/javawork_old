package com.tcs.tree.view;

import com.marconi.fusion.base.asn1.ASN1Exception;
import com.marconi.fusion.base.asn1.ASN1Obj;
import com.marconi.fusion.base.asn1.SetType;

public abstract class AbstractSetTypeUserTreeNode implements UserTreeNode {

	private SetType obj;

	public AbstractSetTypeUserTreeNode(SetType obj) {
		this.obj = obj;
	}

	
	public String getSyntax() {
		if (obj != null) {
			return obj.toString();
		}
		return undefined;
	}

	
	public String getNormalizedData() {
		try {
			return obj.format();
		} catch (Exception e) {
		}
		return undefined;
	}

	public String toString() {
		return getName();
	}

	
	public String getSyntaxName() {
		if (obj != null) {
			return obj.getClass().getSimpleName();
		}
		return undefined;
	}

	
	public abstract String getSpecificInfo();

	
	public abstract int getId();

	
	public abstract String getName();

	
	public String getMember(int memberId) {
		String msg = null;
		if (obj != null) {
			try {
				ASN1Obj member = obj.getMember(memberId);
				if (member != null) {
					msg = member.toString();
				}
			} catch (ASN1Exception e) {
				msg = null;
			}
		}
		return msg;
	}

	
	public String getMember(String memberName) {
		String msg = null;
		if (obj != null) {
			try {
				ASN1Obj member = obj.getMember(memberName);
				if (member != null) {
					msg = member.toString();
				}
			} catch (ASN1Exception e) {
				msg = null;
			}
		}
		return msg;

	}
	
	
	public String[] getMembers() {
		String[] memebers = null;
		if(obj!=null){
			memebers = new String[obj.getNumMembers()];
			for(int i =0; i<obj.getNumMembers();i++){
				try {
					memebers[i] = obj.getMemberName(i);
				} catch (ASN1Exception e) {
					memebers[i]="UN-KNOWN";
				}
			}
		}
		return memebers;
	}

}
