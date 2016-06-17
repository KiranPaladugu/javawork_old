// CLASS SQUARE FILENAME: SQUARE.JAVA
import java.awt.Color;
import java.awt.Graphics;

public class Square extends Shape {
	protected int size;

	public Square() {
		setSide(0);

	}

	public Square(int x, int y, int sX, int sY, int r, int g, int b) {
		setX(x);
		setY(y);
		setColor(r, g, b);
		setSize(sX, sY);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(this.getRedFactor(), this.getBlueFactor(), this.getGreenFactor()));
		g.fillRect(this.getX(), this.getY(), this.getSize(), this.getSize());
	}

	public String getName() {
		return "Square: " + "X: " + this.getX() + "Y: " + this.getY() + "Size: " + this.getSize();
	}

	public int getSize() {
		return size;
	}

	public void setSide(int a) {
		size = (a >= 0 ? a : 0);
	}

	public void setSize(int s1, int s2) {
		size = (s1 <= s2 ? s1 : s2);
	}

}
