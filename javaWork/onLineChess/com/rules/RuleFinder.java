package com.rules;

import java.io.File;
import java.io.IOException;
import com.resources.PropertyFinder;

public class RuleFinder {
	private String key="appRules.fileName";
	private RuleFinder() throws IOException{
		new File(PropertyFinder.getProperty(key));
	}
	public RuleFinder getFinder() throws IOException{
		return new RuleFinder();
	}
}
