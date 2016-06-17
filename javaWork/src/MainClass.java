import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainClass extends JFrame implements MouseListener, WindowListener {
	public static void main(String[] args) // no args expected
	{
		new MainClass();
	} // end main

	JLabel label = new JLabel();

	public MainClass() {
		setSize(200, 300);
		JPanel panel = new JPanel();
		panel.setBackground(Color.CYAN);
		ImageIcon icon = new ImageIcon("H\\0.png");
		label.setIcon(icon);

		label.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.add(label);
		label.addMouseListener(this);
		this.getContentPane().add(panel);
		setVisible(true);
		int i = 0;
		ImageIcon ic = (ImageIcon) label.getIcon();

	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		if (e.getSource().equals(this.label)) {
			label.setBorder(BorderFactory.createLoweredBevelBorder());
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getSource().equals(this.label)) {
			label.setBorder(BorderFactory.createRaisedBevelBorder());
		}
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent e) {
		System.exit(0);

	}

	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}
} // end class ImageDisplay