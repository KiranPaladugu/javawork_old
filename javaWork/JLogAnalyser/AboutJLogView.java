import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 * 
 * @author Kiran
 */
public class AboutJLogView {
	static JWindow window ;
	JButton btn;

	public AboutJLogView() {
		if (window == null) {
			window = new JWindow();			
			Container c = window.getContentPane();
			c.setLayout(new BorderLayout());
			String about = "<html>" + "<body>" + "Created By...<br>" + "KIRAN. <br>" + "<br>" 
					+ "Contact: 91-9885707007<br>" + "E-Mail: paladugukiran@gmail.com<br>" + "Web: <br><br>"
					+ "Version: 1.00<br>" + "Built Date: <br><br><br>" + "</body>" + "</html>";			
			JLabel l = new JLabel("", SwingConstants.LEFT);
			l.setText(about);
			l.setVerticalTextPosition(SwingConstants.TOP);
			l.setIconTextGap(20);
			btn = new JButton("  OK  ");
			btn.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					closeOperation();
				}
			});
			btn.addFocusListener(new FocusListener() {
				
				public void focusLost(FocusEvent e) {
					closeOperation();
				}
				
				public void focusGained(FocusEvent e) {
					
				}
			});
			window.addFocusListener(new FocusListener() {
				
				public void focusLost(FocusEvent e) {
					closeOperation();
				}
				
				public void focusGained(FocusEvent e) {
					
				}
			});
			window.addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent e) {
					
				}
				
				public void mousePressed(MouseEvent e) {
					
				}
				
				public void mouseExited(MouseEvent e) {
					
				}
				
				public void mouseEntered(MouseEvent e) {
					
				}
				
				public void mouseClicked(MouseEvent e) {
					closeOperation();
				}
			}) ;
			c.add(l,BorderLayout.CENTER);
			c.add(btn,BorderLayout.SOUTH);
			c.addFocusListener(new FocusListener() {
				
				public void focusLost(FocusEvent e) {
					closeOperation();
				}
				
				public void focusGained(FocusEvent e) {
					
				}
			});
			int w = 340;
			int h = 250;
			window.setSize(w, h);			
			// set window position
			Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();			
			window.setLocation(center.x - w / 2, center.y - h / 2 + 25);						
			window.setVisible(true);			
			btn.requestFocusInWindow();
		} else {
			window.setVisible(true);
			window.setFocusable(true);
			window.toFront();
		}
	}
	private void closeOperation(){
		window.setVisible(false);
		window = null;
	}
}