package com.pack.brick.ver;

import javax.swing.JPanel;

public class Brick extends JPanel {
	public static final int BREAKABLE=1;
	public static final int UNBREAKABLE=0;
	private static final long serialVersionUID = 1L;
	private boolean breaked;
	private int tough;
	private int brickType;
	public Brick(boolean breaked, int tough, int brickType, boolean breakable) {
		super();
		this.breaked = breaked;
		this.tough = tough;
		this.brickType = brickType;
		this.breakable = breakable;
	}
	public int getBrickType() {
		return brickType;
	}
	public void setBrickType(int brickType) {
		this.brickType = brickType;
	}
	public Brick(boolean breaked, int tough, boolean breakable) {
		super();
		this.breaked = breaked;
		this.tough = tough;
		this.breakable = breakable;
		this.brickType = BREAKABLE;
	}
	public boolean isBreakable() {
		return breakable;
	}
	public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}
	private boolean breakable;
	public Brick() {
		
	}
	public boolean isBreaked() {
		return breaked;
	}
	public void setBreaked(boolean breaked) {
		this.breaked = breaked;
	}
	public int getTough() {
		return tough;
	}
	public void setTough(int tough) {
		this.tough = tough;
	}
}
