import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Clock extends JFrame implements Runnable {
	class ClockPanel extends JPanel {
		public void paintComponent(Graphics painter) {
			Image pic = Toolkit.getDefaultToolkit().getImage("background.jpg");

			if (pic != null)

				painter.drawImage(pic, 0, 0, this); // create image

			// if I didn't use a background image I would have used the setColor
			// and fillRect methods to set background

			painter.setFont(clockFont); // create clock components
			painter.setColor(Color.black);
			painter.drawString(timeNow(), 60, 40);

		}
	}
	// create main method
	public static void main(String[] args) {
		Clock eg = new Clock();
	}

	Thread runner; // declare global objects

	Font clockFont;

	public Clock() {
		super("Java clock");
		setSize(350, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false); // create window

		clockFont = new Font("Serif", Font.BOLD, 40); // create font instance

		Container contentArea = getContentPane();
		ClockPanel timeDisplay = new ClockPanel();

		contentArea.add(timeDisplay); // add components
		setContentPane(contentArea);
		start(); // start thread running

	}

	public void run() {
		while (runner == Thread.currentThread()) {
			repaint();
			// define thread task
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Thread failed");
			}

		}
	}

	public void start() {
		if (runner == null)
			runner = new Thread(this);
		runner.start();
		// method to start thread
	}

	// get current time
	public String timeNow() {
		Calendar now = Calendar.getInstance();
		int hrs = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		int sec = now.get(Calendar.SECOND);
		int milli = now.get(Calendar.MILLISECOND);

		String time = zero(hrs) + ":" + zero(min) + ":" + zero(sec) + ":" + zero(milli / 1000);

		return time;
	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number; // Add leading zero if needed

	}
}
