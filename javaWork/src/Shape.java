// ABSTRACT SUPERCLASS SHAPE.JAVA FILENAMEHAPE.JAVA
import java.awt.Graphics;

public abstract class Shape {
	protected int x, y, redFactor, greenFactor, blueFactor, sizeX, sizeY;

	public Shape() {
		setX(0);
		setY(0);
		setSize(0, 0);
		setColor(0, 0, 0);
	}

	public Shape(int x, int y, int sX, int sY, int r, int g, int b) {
		setX(x);
		setY(y);
		setColor(r, g, b);
		setSize(sX, sY);
	}

	public abstract void draw(Graphics g);

	public int getBlueFactor() {
		return blueFactor;
	}

	public int getGreenFactor() {
		return greenFactor;
	}

	public abstract String getName();

	public int getRedFactor() {
		return redFactor;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setColor(int a, int b, int c) {
		redFactor = ((a >= 0 && a <= 255) ? a : 0);
		greenFactor = ((b >= 0 && b <= 255) ? b : 0);
		blueFactor = ((c >= 0 && c <= 255) ? c : 0);
	}

	public void setSize(int sX, int sY) {
		sizeX = (sX >= 0 ? sX : 0);
		sizeY = (sY >= 0 ? sY : 0);
	}

	public void setX(int a) {
		x = (a >= 0 ? a : 0);
	}

	public void setY(int a) {
		y = (a >= 0 ? a : 0);
	}
}
