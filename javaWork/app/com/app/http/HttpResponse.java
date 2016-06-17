package com.app.http;

public interface HttpResponse {	
	public void sendRedirect(HttpRequest request,HttpResponse response);
	public void setRedirect();
	public void write(String content);
	public void setContent(ResponseContent content);
}
