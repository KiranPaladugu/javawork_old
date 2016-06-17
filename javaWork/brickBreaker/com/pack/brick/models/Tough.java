package com.pack.brick.models;

public class Tough {
	private int tough = 1;

	public Tough() {
	}

	public Tough(int tough) {
		this.tough = tough;
	}

	public int getTough() {
		return this.tough;
	}

	public Tough getToughObj() {
		return new Tough(getTough());
	}

	public void setTough(int type) {
		this.tough = type;
	}

}
