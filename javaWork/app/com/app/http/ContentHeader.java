package com.app.http;

import java.util.Date;

public interface ContentHeader {
	public void setContentHeader(ContentHeader contentHeader);
	public void setResponseCode(int code);
	public void setResposeCode(ResponseCode code);
	public void setResponseDate(Date date);
	public void setServerName(String server);
	public void setContentLength(int length);
	
}
