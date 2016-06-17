package com.pack.hangman.err;

public class HangExceptionHandle {
	public static HangedException unableToLocateData() {
		HangedException hang = new HangedException("Hangman Data Not Found...");
		return hang;
	}

	public String reason;
}
