package com.pack.brick.models;

public abstract class AbstractBrickModel implements BrickModel, Brick {
	private int type;
	private Tough tough = new Tough();
	private boolean breaked = false;

	public Tough getTough() {
		return tough;
	}

	public int getType() {
		return this.type;
	}

	public boolean isBreakable() {
		if (getType() == BRICK_BREAKABLE) {
			return true;
		}
		return false;
	}

	public boolean isBreaked() {
		return breaked;
	}

	public boolean isDestroyed() {
		if (tough.getTough() == 0)
			return true;
		return false;
	}

	public void setTough(Tough tough) {
		this.tough = tough;
	}

	public void setType(int type) {
		this.type = type;
	}

}
