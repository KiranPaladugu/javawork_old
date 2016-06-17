package swings;

import javax.swing.JFrame;

public class FrameWithNoTitleBar {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Removing the Title Bar of a Frame");
	    frame.setUndecorated(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(200, 200, 400, 400);
	    frame.setVisible(true);
	  
	}
}
