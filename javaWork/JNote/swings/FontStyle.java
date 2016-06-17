package swings;

import java.awt.Color;

public class FontStyle {
	private String fontName = "";
	private int style;
	private Color color;
	private int size;
	private boolean underLine;
	private int script = 0;

	public Color getColor() {
		return color;
	}

	public String getFontName() {
		return fontName;
	}

	public int getScript() {
		return script;
	}

	public int getSize() {
		return size;
	}

	public int getStyle() {
		return style;
	}

	public boolean isUnderLine() {
		return underLine;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public void setScript(int script) {
		this.script = script;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public void setUnderLine(boolean underLine) {
		this.underLine = underLine;
	}

	public String toString() {
		String str = "[" + this.getFontName() + "," + this.getStyle() + "," + this.getSize() + "," + this.getColor().toString()
				+ "]";
		return str;
	}
}
