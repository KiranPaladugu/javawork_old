package com.app.reminder.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("rawtypes")
public class Profile extends Properties {
	private static final long serialVersionUID = 8385514770276113183L;

	public static String PATTERN_STRING = "\\$\\{([a-zA-Z0-9_\\-\\|!£\\$\\%\\&\\(\\)\\=\\?\\'\\. \\^]+)\\}";
	public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

	public Profile() {
	}

	public Profile(String filePath) throws IOException {
		InputStream is = new FileInputStream(filePath);
		try {
			load(is);
		} finally {
			is.close();
		}
	}

	public Profile(URL url) throws IOException {
		InputStream is = url.openStream();
		try {
			load(is);
		} finally {
			is.close();
		}
	}

	public Profile(Properties properties) {
		if (properties != null)
			putAll(properties);
	}

	public Profile(Profile profile) {
		if (profile != null)
			putAll(profile);
	}

	public void setFiles(String[] fileNames) {
		for (String file : fileNames)
			setFile(file);
	}

	public void setFile(String fileName) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(fileName.trim());
			innerLoadStream(stream);
		} catch (Exception e) {
		}
	}

	private void innerLoadStream(InputStream stream) {
		try {
			load(stream);
			stream.close();
		} catch (Exception e) {
		}
	}

	
	public synchronized void load(InputStream arg0) throws IOException {
		Properties prop = new Properties();
		prop.load(arg0);

		Enumeration itr = prop.propertyNames();
		while (itr.hasMoreElements()) {
			String oldKey = (String) itr.nextElement();
			String key = substituteMacro(oldKey, prop, false);
			String value = (String) prop.get(oldKey);

			put(key, value);
		}
	}

	public synchronized void load(File file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		load(fileInputStream);
	}

	public synchronized void putAll(Properties properties) {
		Enumeration itr = properties.propertyNames();
		while (itr.hasMoreElements()) {
			String key = (String) itr.nextElement();
			String value = properties.getProperty(key);
			if (value != null)
				setProperty(key, value);
		}
	}

	public synchronized void putAll(Profile profile) {
		Enumeration itr = profile.propertyNames();
		while (itr.hasMoreElements()) {
			String key = (String) itr.nextElement();

			String value = (String) profile.get(key);
			if ((key != null) && (value != null))
				put(key, value);
			else if ((value == null) && (key != null))
				put(key, "");
		}
	}

	public int getIntProperty(Enum<?> key, int defaultValue) {
		return getIntProperty(key.toString(), defaultValue);
	}

	public int getIntProperty(String key, int defaultValue) {
		String value = getProperty(key);
		if (value == null) {

			return defaultValue;
		}

		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
		}

		return defaultValue;
	}

	public long getLongProperty(Enum<?> key, long defaultValue) {
		return getLongProperty(key.toString(), defaultValue);
	}

	public long getLongProperty(String key, long defaultValue) {
		String value = getProperty(key);
		if (value == null) {

			return defaultValue;
		}

		try {
			return Long.parseLong(value);
		} catch (Exception ex) {
		}

		return defaultValue;
	}

	public boolean getBoolProperty(Enum<?> key, boolean defaultValue) {
		return getBoolProperty(key.toString(), defaultValue);
	}

	public boolean getBoolProperty(String key, boolean defaultValue) {
		String value = getProperty(key);
		if (value == null) {

			return defaultValue;
		}

		return (value.trim().toUpperCase().compareTo("TRUE") == 0) || (value.trim().toUpperCase().compareTo("YES") == 0);
	}

	public long getTimeProperty(Enum<?> key, String defaultValue) {
		long millis = TimeConverter.toMillis(defaultValue);
		return getTimeProperty(key.toString(), millis);
	}

	public long getTimeProperty(String key, String defaultValue) {
		long millis = TimeConverter.toMillis(defaultValue);
		return getTimeProperty(key, millis);
	}

	public long getTimeProperty(Enum<?> key, long defaultValue) {
		return getTimeProperty(key.toString(), defaultValue);
	}

	public long getTimeProperty(String key, long defaultValue) {
		String value = getProperty(key);

		if (value == null) {
			return defaultValue;
		}
		long millis = TimeConverter.toMillis(value);

		return millis;
	}

	public String getProperty(Enum<?> key) {
		return getProperty(key.toString());
	}

	public String getProperty(Enum<?> key, String defaultValue) {
		return getProperty(key.toString(), defaultValue);
	}

	public Object setProperty(Enum<?> key, String value) {
		return setProperty(key.toString(), value);
	}

	public Object setProperty(String key, String value) {
		Object obj = super.setProperty(key, value);
		return obj;
	}

	public String getProperty(String key) {
		String value = super.getProperty(key);
		if (value == null) {
			return null;
		}
		return substituteMacro(value.trim(), this, false);
	}

	public String getProperty(String key, String defaultValue) {
		String value = super.getProperty(key, defaultValue);

		return substituteMacro(value.trim(), this, false);
	}

	public synchronized Object get(Object key) {
		Object obj = super.get(key);
		if ((obj == null) || (!(obj instanceof String))) {
			return null;
		}
		String value = substituteMacro((String) obj, this, false);

		return value;
	}

	private static String substituteMacro(String value, Properties inputProfile, boolean stripUnmatchedValue) {
		value = value.trim();

		Properties profile = inputProfile;

		Matcher matcher = PATTERN.matcher(value);

		String substituted = value;

		if (matcher.find()) {
			String group = matcher.group();
			int start = matcher.start();
			int end = matcher.end();

			String possibleKey = group.substring(2, group.length() - 1).trim();

			String possibleValue = (String) profile.get(possibleKey);

			if (possibleValue == null) {
				String substituteMacro = substituteMacro(substituted.substring(end), profile, stripUnmatchedValue);

				String firstPiece = null;
				if (stripUnmatchedValue)
					firstPiece = substituted.substring(0, start);
				else {
					firstPiece = substituted.substring(0, end);
				}
				substituted = firstPiece + substituteMacro;
			} else {
				String tmp1 = value.substring(0, start);
				String tmp2 = possibleValue;
				String tmp3 = value.substring(end, value.length());
				substituted = substituteMacro(tmp1 + tmp2 + tmp3, profile, stripUnmatchedValue);
			}

		}

		return substituted;
	}

	public synchronized Profile clone() {
		Profile profile = new Profile();

		Set keySet2 = keySet();

		for (Iterator itr = keySet2.iterator(); itr.hasNext();) {
			Object object = itr.next();
			Object object2 = super.get(object);
			profile.put(object, object2);
		}

		return profile;
	}

	public synchronized int hashCode() {
		return super.hashCode();
	}

	public synchronized boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profile other = (Profile) obj;

		Set keySet2 = other.keySet();
		for (Iterator itr = keySet2.iterator(); itr.hasNext();) {
			Object key = itr.next();
			Object myValue = get(key);
			Object otherValue = other.get(key);

			if ((myValue == null) || (otherValue == null)) {
				return false;
			}

			if ((myValue != null) && (otherValue != null) && (!otherValue.equals(myValue))) {
				return false;
			}

		}

		Set keySet3 = keySet();
		for (Iterator itr = keySet3.iterator(); itr.hasNext();) {
			Object key = itr.next();
			Object myValue = get(key);
			Object otherValue = other.get(key);

			if ((myValue == null) || (otherValue == null)) {
				return false;
			}

			if ((myValue != null) && (otherValue != null) && (!otherValue.equals(myValue))) {
				return false;
			}
		}

		return true;
	}

	@Deprecated
	public Properties getMacroSubstitutedProperties(boolean stripUnmatchedValues) {
		Properties substitutedProperties = getMacroSubstitutedProperties();
		Properties properties = new Properties();

		Set keySet = substitutedProperties.keySet();
		for (Iterator itr = keySet.iterator(); itr.hasNext();) {
			Object key = itr.next();

			String value = substituteMacro((String) substitutedProperties.get(key), this, stripUnmatchedValues);
			properties.put(key, value);
		}
		return properties;
	}

	@Deprecated
	public Properties getMacroSubstitutedProperties() {
		Properties props = new Properties();

		Set keys = keySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Object value = get(key);

			if (value != null) {
				props.put(key, value);
			}
		}

		return props;
	}

	public static final class PropertiesBuilder {
		private final Profile profile;
		private boolean stripUnmatchedMacros = false;
		private boolean keepEmptyValues = true;

		public PropertiesBuilder(Profile profile) {
			this.profile = profile;
		}

		public boolean isStripUnmatchedMacros() {
			return this.stripUnmatchedMacros;
		}

		public PropertiesBuilder setStripUnmatchedMacros(boolean stripUnmatchedMacros) {
			this.stripUnmatchedMacros = stripUnmatchedMacros;
			return this;
		}

		public boolean isKeepEmptyValues() {
			return this.keepEmptyValues;
		}

		public PropertiesBuilder setKeepEmptyValues(boolean skipUnmatchedValues) {
			this.keepEmptyValues = skipUnmatchedValues;
			return this;
		}

		private Properties filterEmptyValues(Properties properties) {
			Properties props = properties;
			if (!this.keepEmptyValues) {
				props = new Properties();

				Set keys = properties.keySet();
				Iterator iter = keys.iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					String value = (String) properties.get(key);

					if ((value != null) && (!value.trim().equals(""))) {
						props.put(key, value.trim());
					}
				}
			}

			return props;
		}

		public Properties buildProperties() {
			Properties properties = this.profile.getMacroSubstitutedProperties(this.stripUnmatchedMacros);

			Properties filterProperties = filterEmptyValues(properties);

			return filterProperties;
		}
	}
}