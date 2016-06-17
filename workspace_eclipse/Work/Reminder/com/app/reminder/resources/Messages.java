package com.app.reminder.resources;

public class Messages {
	
	public static String INT_PROPERTY = "";
	
	public static enum Key {
		
		Application_Name("Application_2$.name"),
		Application_Intances_MaxNumber("Application_1$.instances.maxnumber")
		;
		private String value = null;

		private Key(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}
}
