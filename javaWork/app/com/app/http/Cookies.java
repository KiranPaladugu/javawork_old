package com.app.http;

public interface Cookies {
	public  Cookie getCookie(String name);
	public void setCookie(Cookie cookie);
	public Cookie[] getAllCookies();
}
