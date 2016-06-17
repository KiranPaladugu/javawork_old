package com.framework.utils;

import java.io.Serializable;
import java.util.Date;

public interface License extends Serializable  {
	public static final int EVOLUTION_TYPE = 0;
	public static final int FULL_TYPE = 1;
	public static final int UNLIMITED_TYPE = 2;
	public static final int LIMITED_TYPE = 3;
	public Date startDate();
	public Date endDate();
	public boolean isExpired();
	public int getRemainingDays();
	public int getLicenseDays();
	public String getSerial();
	public boolean isEvalutionLicense();
	public boolean isFullLicense();
	public int getLicenseType();
	public int getVersion();
	public String getFootPrint();
	public String getName();
	public void setSerial(String serial);
	public void setName(String Name);
}
