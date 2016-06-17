import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

public class TimerTest extends JFrame {

	public static void main(String[] args) {
		new TimerTest().setVisible(true);
	}

	private Timer timer;

	private JButton button;
	private final int DELAY = 600;
	public TimerTest() {
		setSize(300, 300);
		setTitle("Timer test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // For closing esasily
		// the JFrame
		timer = new Timer(DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setText("Running.........\nPlease Wait....");
			}

		});
		timer.setRepeats(true);

		button = new JButton("Run !!!");
		add(button);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				timer.start();
			}

		});

	}

}
