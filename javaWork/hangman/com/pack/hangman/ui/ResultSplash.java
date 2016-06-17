package com.pack.hangman.ui;

import javax.swing.JWindow;

public class ResultSplash {
	private static final long serialVersionUID = 1L;
	public static final int WON = 1;
	public static final int LOST = 2;
	private JWindow window;
	private String message = "";

	public ResultSplash(int type) {
		switch (type) {
		case WON:
			message = "CONGRATS !! YOU WON !!";
			break;
		case LOST:
			message = "YOU LOOSER !!";
			break;
		default:
		}
	}

	private void animation(int time) {
		try {
			Thread.sleep(time);
			window.dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void showWindow() {
		/*
		 * window = new JWindow(); JLabel label=new JLabel(message);
		 * label.setForeground(Color.black); window.add(label); int wid=350; int
		 * het=350; Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
		 * int x=(dim.width-wid)/2; int y=(dim.height-het)/2;
		 * window.setBounds(x,y,wid,het); window.setSize(wid, het);
		 * window.repaint(); window.setVisible(true); this.animation(10000);
		 */
	}
}
