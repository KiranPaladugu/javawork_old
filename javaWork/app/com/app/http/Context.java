package com.app.http;

public interface Context {
	public String parameter(String parameter);
	public String getContextParameter(String parameter);
	public String getContextPath();
}
