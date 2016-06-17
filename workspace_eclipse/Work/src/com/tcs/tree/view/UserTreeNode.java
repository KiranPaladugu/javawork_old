package com.tcs.tree.view;

public interface UserTreeNode {
	public static final String undefined ="<UN-DEFINED>";
	public static final String empty ="<EMPTY!>";
	public static final String NULL = "null";
	public String getSyntax();
	public String getNormalizedData();
	public String getName();
	public int getId();
	public String getSyntaxName();
	public String toString();
	public String getSpecificInfo();
	public String getMember(int memberId);
	public String getMember(String memberName);
	public String[] getMembers();
}
