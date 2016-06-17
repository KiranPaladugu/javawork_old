package com.data.model;
public class LoginDetails implements ClientData {
	private static final long serialVersionUID = -7281581925121132934L;
	private String name;
	private String password;
	public Class getClassName() {		
		return LoginDetails.class;
	}
	public LoginDetails(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	
}
