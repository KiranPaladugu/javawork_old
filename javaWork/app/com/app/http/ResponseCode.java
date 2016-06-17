package com.app.http;

public interface ResponseCode {
	public String getAlias(int code);
	public int getCode(String codeAlias);
	public int[] getAllCodes();
	public String[] getAll();
}
