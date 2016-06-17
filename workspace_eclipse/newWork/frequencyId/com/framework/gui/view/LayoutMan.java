package com.framework.gui.view;

import java.awt.Rectangle;

public class LayoutMan {
	public static final int LAYOUTMAN_RELATIVE = 0;
	public static final int LAYOUTMAN_ABSOLUTE = 1;
	public static final int LAYOUTMAN_NEW = 10;
	public static final int LAYOUTMAN_OLD = 11;
	public static final int LAYOUTMAN_HORIZANTAL_ALIGN = 20;
	public static final int LAYOUTMAN_VERTICAL_ALIGN = 21;

	private int x_gap = 1;

	public int getX_gap() {
		return x_gap;
	}

	public void setX_gap(int x_gap) {
		this.x_gap = x_gap;
	}

	public int getY_gap() {
		return y_gap;
	}

	public void setY_gap(int y_gap) {
		this.y_gap = y_gap;
	}

	private int y_gap = 1;
	private int x = 0;
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private int y = 0;
	private int count = 0;
	private Rectangle rectangle;

	public LayoutMan() {
		rectangle = new Rectangle();
	}

	public Rectangle getBounds(int width, int height) {
		count++;
		return getHorizonatlAlign(width, height);
	}

	public Rectangle getBounds(int width, int height, int optons) {
		count++;
		if (optons == LAYOUTMAN_HORIZANTAL_ALIGN)
			return getHorizonatlAlign(width, height);
		else if(optons == LAYOUTMAN_VERTICAL_ALIGN)
			return getVerticalAlign(width, height);
		else
			
			return null;
	}

	/**
	 * @param optons
	 */
	@SuppressWarnings("unused")
	private void identify(int width, int height, int optons) {
		switch (optons) {
		case LAYOUTMAN_ABSOLUTE:
			break;
		case LAYOUTMAN_RELATIVE:
			break;
		case LAYOUTMAN_HORIZANTAL_ALIGN:
			break;
		case LAYOUTMAN_VERTICAL_ALIGN:
			break;
		case LAYOUTMAN_NEW:
			break;
		case LAYOUTMAN_OLD:
			break;
		default:
			break;
		}
	}

	private Rectangle getVerticalAlign(int width, int height) {
		Rectangle rect = rectangle;
		rectangle = new Rectangle();
		//x += x_gap;
		y += y_gap;

		rectangle.x = x;
		rectangle.y=y+=rect.height;
		rectangle.width = width;
		rectangle.height = height;
		return rectangle;
	}

	private Rectangle getHorizonatlAlign(int width, int height) {
		Rectangle rect = rectangle;
		rectangle = new Rectangle();
		x += x_gap;
		//y += y_gap;

		rectangle.x = x += rect.width;
		rectangle.y=y;
		rectangle.width = width;
		rectangle.height = height;
		return rectangle;

	}

	public int getUsageCount() {
		return count;
	}
}
