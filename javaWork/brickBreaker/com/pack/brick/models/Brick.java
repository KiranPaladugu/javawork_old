package com.pack.brick.models;

import java.awt.Dimension;

public interface Brick extends TouchableObject{

	public void breakBrick();

	public void destroy();

	public Dimension getBrickSize();

	public BrickModel getModel();

	public void getPosition();

	public Tough getTough();

	public int getType();

	public boolean isBreakable();

	public boolean isBreaked();

	public boolean isDestroyed();

	public void setBrickSize(Dimension dimension);

	public void setBrickSize(int width, int height);

	public void setModel(BrickModel model);

	public void setPosition();

	public void setTough(Tough tough);

	public void setType(int type);
}
