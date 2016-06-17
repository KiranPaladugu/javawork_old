import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestRunner {
	public static void main(String[] args) throws InterruptedException {
		long mi = System.currentTimeMillis();
		final JFrame frame = new JFrame();
		JLabel title = new JLabel("Current Time :");
		JLabel lbl = new JLabel();
		frame.setUndecorated(true);
		frame.add(title);
		frame.add(lbl);
		frame.setSize(200,60);
		frame.setIconImage(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e) {
				
			}
			
			public void windowIconified(WindowEvent e) {
				
			}
			
			public void windowDeiconified(WindowEvent e) {				
			}
			
			public void windowDeactivated(WindowEvent e) {
				
			}
			
			public void windowClosing(WindowEvent e) {
				
			}
			
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
			
			public void windowActivated(WindowEvent e) {
				
			}
		});
		while(true){
			lbl.setText("Current Time :"+(mi/1000)+"."+(mi%1000));
			mi++;
			Thread.sleep(1);
		}
	}
}
