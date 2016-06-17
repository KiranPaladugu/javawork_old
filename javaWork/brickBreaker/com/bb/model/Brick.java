package com.bb.model;

import java.awt.Component;

import javax.swing.JPanel;

public class Brick extends JPanel {
	private static final long serialVersionUID = -1649896887601484175L;
	public static final int BREAKABLE_BRICK = 1;
	public static final int UNBREAKABLE_BRICK = 0;
	private int tough = 1;
	private boolean isBreakable = true;
	private boolean isDestroyed = false;
	private int brickType = BREAKABLE_BRICK;

	public Brick() {
	}

	public int getBrickType() {
		return brickType;
	}

	public void setBrickType(int brickType) {
		this.brickType = brickType;
	}

	public Brick(int tough, boolean isBreakable, boolean isDestroyed, int brickType) {
		super();
		this.tough = tough;
		this.isBreakable = isBreakable;
		this.isDestroyed = isDestroyed;
		this.brickType = brickType;
	}

	public int gettough() {
		return this.tough;
	}

	public void settough(int tough) {
		this.tough = tough;
	}

	public boolean isBreakable() {
		return isBreakable;
	}

	public void setBreakable(boolean isBreakable) {
		this.isBreakable = isBreakable;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public Component getComponent() {
		return this;
	}

	public void hit() {
		if (brickType == BREAKABLE_BRICK) {
			if (tough > 0) {
				tough--;
				if (tough == 0) {
					this.setDestroyed(true);
				}
			} else if (tough < 1) {
				this.setDestroyed(true);
			}
		}
	}
}
