

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4375615735275026933L;
	JLabel caps;
	JLabel num;
	JLabel scrol;
	JLabel line;
	JLabel col, position;

	JPanel lockPanel;

	JPanel cursorPanel;

	public StatusBar() {
		this.setLayout(new GridLayout());
		lockPanel = new JPanel(new BorderLayout());
		cursorPanel = new JPanel();

		caps = new JLabel(" ");
		caps.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		num = new JLabel(" ");
		num.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		scrol = new JLabel(" ");
		scrol.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		position = new JLabel("Position:0");
		position.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lockPanel.add(caps, BorderLayout.EAST);
		lockPanel.add(num, BorderLayout.CENTER);
		lockPanel.add(scrol, BorderLayout.WEST);

		line = new JLabel("Line:0");
		line.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		col = new JLabel("Col:0");
		col.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		cursorPanel.setLayout(new BorderLayout());
		cursorPanel.add(line, BorderLayout.EAST);
		cursorPanel.add(col, BorderLayout.WEST);
		cursorPanel.add(position, BorderLayout.CENTER);

		this.add(cursorPanel);
		this.add(lockPanel);
		this.setBorder(BorderFactory.createLoweredBevelBorder());

	}
	public int getCol() {
		int col = -1;
		String str = this.col.getText();
		str = str.trim();
		try {
			col = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return col;
	}

	public int getLine() {
		int line = -1;
		String str = this.line.getText();
		str = str.trim();
		try {
			line = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return line;
	}

	public int getPosition() {
		int col = -1;
		String str = this.position.getText();
		str = str.trim();
		try {
			col = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return col;
	}

	public void setCaps(boolean flag) {
		if (flag) {
			this.caps.setText("CAPS");
		} else
			caps.setText(" ");
	}

	public void setCol(int col) {
		this.col.setText("Col:" + col);
	}

	public void setLine(int line) {
		this.line.setText("Line:" + line);
	}

	public void setNum(boolean flag) {
		if (flag) {
			num.setText("NUMS");
		} else
			num.setText(" ");
	}

	public void setPosition(int position) {
		this.position.setText("Position:" + position);
	}

	public void setScroll(boolean flag) {
		if (flag) {
			this.scrol.setText("SCROL");
		} else
			scrol.setText(" ");
	}
}
