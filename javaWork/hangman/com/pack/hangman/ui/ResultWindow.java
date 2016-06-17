package com.pack.hangman.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.UIManager;

public class ResultWindow extends JWindow implements KeyListener, ActionListener, MouseListener {
	public static final int LOST = 0;
	public static final int WIN = 1;
	public static final int UNKNOWN = -1;
	public static final int RETREAT = 2;
	public static void main(String[] args) {
		new ResultWindow(ResultWindow.LOST);
	}
	private String msg = "";

	private JProgressBar progresbar;

	public ResultWindow() {

	}

	public ResultWindow(int type) {
		switch (type) {
		case LOST:
			msg = "You LOOSER !!";
			break;
		case WIN:
			msg = "You WON !!";
			break;
		case UNKNOWN:
			msg = "You LOOSER !!";
			break;
		case RETREAT:
			msg = "You LOOSER !!";
			break;
		default:
			msg = "You LOOSER !!";
			break;
		}
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		init();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	private void init() {
		Toolkit toolkit = this.getToolkit();
		Image image = Toolkit.getDefaultToolkit().getImage("ing/Lost.PNG");
		ImageIcon icon = new ImageIcon(image);
		JLabel lable = new JLabel("");
		lable.setIcon(icon);
		this.add(lable);
		progresbar = new JProgressBar(1, 100);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.add(progresbar);
		this.setSize(700, 500);
		this.setVisible(true);
		// progresbar.setUI(new ProgressBarUI() {});
		int n = 1;
		while (n <= 100) {
			progresbar.setValue(n);
			System.out.println("N Valu is :" + n);
			// this.repaint();
			n++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.dispose();

	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("You Pressed :" + e.getKeyChar() + " :code:" + e.getKeyCode());

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
