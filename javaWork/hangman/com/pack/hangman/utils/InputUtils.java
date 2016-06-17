package com.pack.hangman.utils;

import java.awt.List;

import com.pack.hangman.log.Logger;

public class InputUtils {
	public static String msg = "";

	public static String w = "";
	public static int mistakes = 0;
	public static boolean f;
	public static String createView(String str) {
		String msg = "";
		for (int i = 0; i < str.length(); i++) {
			msg += " _";
		}
		// Logger.log("String is:"+str.toUpperCase());
		Logger.log("Message is :" + msg.trim());
		return msg.trim();
	}

	public static String evaluateInputAndView(String in, String word, char input) {
		String returnString = "";
		String s = null, v = null;
		boolean flag = false;
		// Logger.log("input:"+input);
		input = new String("" + input).toUpperCase().charAt(0);
		Logger.log("input:" + input);
		s = new String(word).toUpperCase();
		v = new String(in).toUpperCase();
		Logger.log("view :" + v + " : Length:" + v.length());
		// Logger.log("word :"+s+" : Lenght:"+s.length());
		for (int k = 0; k < s.length(); k++) {
			if (s.charAt(k) == input) {
				// Logger.log("Word contain given input  :-)");
				for (int i = 0; i <= msg.length(); i++) {
					{
						if (msg.length() == 0) {
							msg += "" + input;
							flag = true;
							// Logger.log("Now total input :"+msg);
							break;
						} else if (msg.charAt(i) == input) {
							Logger.log("Already present ... check for another");
						} else {
							// Logger.log("Not present..");
							msg += "" + input;
							flag = true;
							// Logger.log("Now total input :"+msg);
							break;
						}
					}
				}
				break;
			} else {
				flag = false;
				// Logger.log("Word doesnt contain given input  :-(");
			}
		}
		if (!flag) {
			mistakes++;
			Logger.log("MISTAKE!!! no:" + mistakes);
		}
		returnString = setOccurance(s, in, input);
		return returnString;
	}

	public static boolean isCompleted(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '_') {
				return false;
			}
		}
		return true;
	}

	public static boolean isDuplicate(List list, String newString) {
		String[] li = list.getItems();
		for (int i = 0; i < li.length; i++) {
			if (li[i].equalsIgnoreCase(newString)) {
				return true;
			}
		}
		return false;
	}

	public static String removeChar(String s, char c) {
		String r = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != c)
				r += s.charAt(i);
		}
		return r;
	}

	public static String setCompletedView(String word) {
		String str = "";
		word = word.toUpperCase();
		for (int i = 0; i < word.length(); i++) {
			if (i == word.length() - 1) {
				str += word.charAt(i);
			} else {
				str += word.charAt(i) + " ";
			}
		}
		str.trim();
		return str;
	}

	public static String setOccurance(String actual, String toModify, char ch) {
		String str = "";
		char c[] = toModify.toCharArray();
		for (int i = 0; i < actual.length(); i++) {
			if (actual.charAt(i) == ch) {
				int x = (2 * (i + 1)) - 2;
				c[x] = ch;
			}
		}
		return new String(c);
	}
}
