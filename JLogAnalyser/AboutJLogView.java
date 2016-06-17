import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 
 * @author Kiran
 */
public class AboutJLogView {
	static JFrame window ;
	Notepad samp;
	JButton btn;

	public AboutJLogView() {
		if (window == null) {
			window = new JFrame("About JLogAnalyser");
			Container c = window.getContentPane();
			c.setLayout(new FlowLayout());
			String about = "<html>" + "<body>" + "Created By...<br>" + "KIRAN. <br>" + "<br>" + "<br>" + "<br><br><br>"
					+ "Contact: 91-9885707007<br>" + "E-Mail: paladugukiran@gmail.com<br>" + "Web: <br><br>"
					+ "Version: 1.00<br>" + "Built Date: <br><br><br>" + "</body>" + "</html>";
			ImageIcon icon = new ImageIcon("img"+File.separator+"bold.png");
			JLabel l = new JLabel("", icon, SwingConstants.LEFT);
			l.setText(about);
			l.setVerticalTextPosition(SwingConstants.TOP);
			l.setIconTextGap(20);
			c.add(l);
			int w = 340;
			int h = 250;
			window.setSize(w, h);
			window.setResizable(false);
			// set window position
			Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();			
			window.setLocation(center.x - w / 2, center.y - h / 2 + 25);			
			window.setIconImage(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"2eye.png"));
			window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			window.setVisible(true);
		} else {
			window.setVisible(true);
		}
	}
}