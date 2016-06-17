package com.pack.hangman.server.update.ui;

import java.awt.List;

public class ServerUpdateUtils {
	public static boolean isDuplicate(List list, String newString) {
		String[] li = list.getItems();
		for (int i = 0; i < li.length; i++) {
			if (li[i].equalsIgnoreCase(newString)) {
				return true;
			}
		}
		return false;
	}

}
