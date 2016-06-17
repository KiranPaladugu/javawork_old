package com.tcs.log.freq.resources;

public class PropertyFinderException extends Exception {
	private static final long serialVersionUID = 1L;
	public PropertyFinderException() {
		super();
	}
	public PropertyFinderException(String reason){
		super(reason);		
	}
	public PropertyFinderException(Throwable cause){
		super(cause);
	}
	public PropertyFinderException(String message,Throwable cause){
		super(message,cause);
	}
}
