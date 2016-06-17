//RECTANGLE CLASS FILENAME: RECTANGLE.JAVA
import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Shape {
	protected int length, width;

	public Rectangle() {
		setSize(0, 0);
	}

	public Rectangle(int x, int y, int sX, int sY, int r, int g, int b) {
		setX(x);
		setY(y);
		setColor(r, g, b);
		setSize(sX, sY);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(this.getRedFactor(), this.getGreenFactor(), this.getBlueFactor()));
		g.fillRect(this.getX(), this.getY(), this.getLength(), this.getWidth());
	}

	public int getLength() {
		return length;
	}

	public String getName() {
		return "Rectangle: " + "X: " + this.getX() + "Y: " + this.getY() + "length: " + this.getLength() + "height: "
				+ this.getWidth();
	}

	public int getWidth() {
		return width;
	}

	public void setSize(int sX, int sY) {
		length = (sX >= 0) ? sX : 0;
		width = (sY >= 0) ? sY : 0;
	}
}
