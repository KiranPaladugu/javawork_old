package com.pack.hangman.err;

public class HangedException extends Exception {
	private String reason = null;

	public HangedException() {
		super();
	}

	public HangedException(String reason) {
		super(reason);
		this.reason = reason;
	}
}
