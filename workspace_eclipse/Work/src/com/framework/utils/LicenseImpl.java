package com.framework.utils;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

public class LicenseImpl implements License {
	private static final long serialVersionUID = 1L;
	private String footPrint;
	private String name = "";
	private Date start;
	private Date end;
	private int licenseType;
	private int version;
	private String serial;

	@SuppressWarnings("deprecation")
	public LicenseImpl(String footPrint, int licenseType, int version) {
		this.footPrint = footPrint;
		this.licenseType = licenseType;
		this.version = version;
		if (licenseType == EVOLUTION_TYPE) {
			start = new Date();
			long inc = (10 * 1000 * 60 * 60 * 24);
			long var = start.getTime() + inc;
			end = new Date(var);
		} else if (licenseType == FULL_TYPE) {
			start = new Date();			
			end = new Date();
			end.setYear(end.getYear()+1);
		} else if (licenseType == UNLIMITED_TYPE) {
			start = null;
			end = null;
		} else if (licenseType == LIMITED_TYPE) {
			start = null;
			end = null;
		} else {
			this.footPrint = null;
			this.licenseType = -1;
			this.version = -1;
		}
	}

	public LicenseImpl(String footPrint, int version) {
		this.footPrint = footPrint;
		this.version = version;
		start = new Date();
		long var = start.getTime() + (10 * (24 * (60 * (60 * (1000)))));
		end = new Date(var);
		licenseType = EVOLUTION_TYPE;
	}

	public Date endDate() {
		return end;
	}

	public int getLicenseDays() {
		int remaining = -1;
		if ((start != null) && (end != null)) {
			long rem = end.getTime() - start.getTime();
			remaining = (int) (rem / (1000 * 60 * 60 * 24));
		}
		return remaining;
	}

	public int getLicenseType() {
		return licenseType;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public int getRemainingDays() {
		int remaining = -1;
		if (end != null) {
			long rem = end.getTime() - new Date().getTime();
			remaining = (int) (rem / (1000 * 60 * 60 * 24));
		}
		return remaining;
	}

	public String getSerial() {
		return serial;
	}

	public int getVersion() {
		return version;
	}

	public boolean isEvalutionLicense() {
		if (licenseType == EVOLUTION_TYPE) {
			return true;
		} else
			return false;
	}

	public boolean isExpired() {
		if (getRemainingDays() < 0)
			return true;
		else
			return false;
	}

	public boolean isFullLicense() {
		if (licenseType == FULL_TYPE) {
			return true;
		} else
			return false;
	}

	public Date startDate() {
		return start;
	}

	public String getFootPrint() {
		return footPrint;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
