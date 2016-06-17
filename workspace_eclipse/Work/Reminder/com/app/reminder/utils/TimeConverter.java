package com.app.reminder.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter
{
  static final char HOURS = 'h';
  static final char MINUTES = 'm';
  static final char SECONDS = 's';
  static final char MILLIS = 'S';
  static final long MILLIS_PER_SECOND = 1000L;
  static final long MILLIS_PER_MINUTE = 60000L;
  static final long MILLIS_PER_HOUR = 3600000L;

  public static long toMillis(String value)
  {
    long millis = -1L;
    if (value.indexOf(':') != -1)
      millis = parseAsDate(value);
    else {
      millis = parseAsSimpleFormat(value);
    }
    return millis;
  }

  protected static long parseAsSimpleFormat(String value) {
    value = value.trim();
    int len = value.length();
    char tu = value.charAt(len - 1);

    if (Character.isDigit(tu)) {
      long millis = Long.parseLong(value);
      return millis;
    }

    value = value.substring(0, len - 1).trim();
    long millis = Long.parseLong(value);

    switch (tu) {
    case 'h':
      millis *= 3600000L;
      break;
    case 'm':
      millis *= 60000L;
      break;
    case 's':
      millis *= 1000L;
      break;
    case 'S':
      break;
    default:
      throw new IllegalArgumentException("Invalid specifier <" + tu + ">");
    }

    return millis;
  }

  protected static long parseAsDate(String value) {
    String format = "h:m:s";
    if (value.indexOf('.') != -1) {
      format = "h:m:s.S";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);

    long time = 0L;
    try {
      Date dt = sdf.parse(value);
      time = dt.getTime();
    }
    catch (Exception e) {
    }
    return time;
  }
}