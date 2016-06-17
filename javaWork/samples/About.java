import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 
 * @author Kiran
 */
public class About {
	static JFrame window = new JFrame("About JLogAnalyser");
	Notepad samp;
	JButton btn;

	public About(Notepad ref) {
		samp = ref;
		Container c = window.getContentPane();
		c.setLayout(new FlowLayout());

		String about = "<html>" + "<body>" + "Created By...<br>" + "KIRAN. <br>" + "<br>" + "<br>" + "<br><br><br>"
				+ "Contact: 91-9885707007<br>" + "E-Mail: paladugukiran@gmail.com<br>" + "Web: <br><br>" + "Version: 1.00<br>"
				+ "Built Date: <br><br><br>" + "</body>" + "</html>";

		ImageIcon icon = new ImageIcon("author.jpg");
		JLabel l = new JLabel("", icon, SwingConstants.LEFT);
		l.setText(about);
		l.setVerticalTextPosition(SwingConstants.TOP);
		l.setIconTextGap(20);
		c.add(l);
		int w = 340;
		int h = 250;
		window.setSize(w, h);
		// set window position
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		window.setLocation(center.x - w / 2, center.y - h / 2 + 25);
		window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		window.setVisible(false);

	}
}