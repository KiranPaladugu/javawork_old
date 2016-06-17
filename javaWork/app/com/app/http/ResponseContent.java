package com.app.http;

public interface ResponseContent {
	public void add(String str);
	public void setContent(String content);
	public void getContent(String content);
	public int getContentLength();
	
}
