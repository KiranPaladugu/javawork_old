package com.pack.hangman.utils;

import java.util.Random;

import com.pack.hangman.data.DataReader;
import com.pack.hangman.log.Logger;

public class WordFetcher {
	public static String getNewWord() {
		DataReader reader = new DataReader();
		String str[] = reader.getData();
		java.util.Random rand = new Random();
		int first = rand.nextInt(str.length);
		int second = rand.nextInt(str.length);
		int third = rand.nextInt(str.length);
		int forth = rand.nextInt(str.length);
		int choice = rand.nextInt(3);
		int index = str.length;
		switch (choice) {
		case 0:
			index = first;
			Logger.log("Case0");
			break;
		case 1:
			index = second;
			Logger.log("Case1");
			break;
		case 2:
			index = third;
			Logger.log("Case2");
			break;
		case 3:
			index = forth;
			Logger.log("Case3");
			break;
		default:
			index = str.length;
			Logger.log("default");
		}
		Logger.log("in :" + index);
		return str[index];
	}

}
