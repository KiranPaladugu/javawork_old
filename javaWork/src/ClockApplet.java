import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Date;

public class ClockApplet extends Applet implements Runnable {
	Thread goThread;
	static Image doubleBufferImage; // the hidden buffer
	static Graphics doubleBufferG;

	public void init() {
		goThread = new Thread(this);
		goThread.start();
	}

	public void paint(Graphics g) {
		Date d = new Date(System.currentTimeMillis());
		g.drawString(d.getHours() + ":" + d.getMinutes() + ':' + d.getSeconds(), 10, 10);
	}

	public void run() {
		while (Thread.currentThread() == goThread) {
			repaint();
		}
	}

	public void update(Graphics g) // update does the dubble buffering.
	{
		if (doubleBufferImage == null) // if this is the first time, create the
		// buffer
		{
			doubleBufferImage = createImage(this.getSize().width, this.getSize().height);
			doubleBufferG = doubleBufferImage.getGraphics();
		}
		doubleBufferG.setColor(getBackground()); // clear the screen
		doubleBufferG.fillRect(0, 0, this.getSize().width, this.getSize().height);
		doubleBufferG.setColor(getForeground()); // draw to the hidden buffer
		paint(doubleBufferG);
		g.drawImage(doubleBufferImage, 0, 0, this); // move the hidden buffer to
		// the screen
	}
}
