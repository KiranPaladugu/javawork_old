// CIRCLE CLASS BEGINS HERE: FILENAME(OBVIOUSLY): //CIRCLE.JAVA

import java.awt.Color;
import java.awt.Graphics;

public class Circle extends Shape {
	protected int radius;

	public Circle() {
		setRadius(0, 0);
	}

	public Circle(int x, int y, int sX, int sY, int r, int g, int b) {
		setX(x);
		setY(y);
		setRadius(sX, sY);
		setColor(r, g, b);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(this.getRedFactor(), this.getGreenFactor(), this.getBlueFactor()));
		g.fillOval(this.getX(), this.getY(), this.getRadius(), this.getRadius());
	}

	public String getName() {
		return "Circle: " + " X: " + this.getX() + " Y: " + this.getY() + " Radius: " + this.getRadius();

	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int a, int b) {
		radius = (a <= b) ? a : b;
		radius = (radius >= 0) ? radius : 0;
	}
}