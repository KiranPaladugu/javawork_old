import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ViewerFrame extends JFrame implements WindowListener, WindowStateListener, WindowFocusListener {
	private static final long serialVersionUID = 1L;
	private Container c;
	private ViewerPanel viewer;
	private MenuView menu;
	public ViewerFrame() {
		super();
		init();
	}

	private void init() {
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		viewer = new ViewerPanel();
		menu = new MenuView(viewer.getView());
		c.add(viewer, BorderLayout.CENTER);
		this.addWindowFocusListener(this);
		this.addWindowListener(this);
		this.addWindowStateListener(this);
		this.setJMenuBar(menu);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("img"+File.separator+"2eye.png"));
		int wid = 800;
		int het = 600;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - wid) / 2;
		int y = (dim.height - het) / 2;
		setBounds(x, y, wid, het);
		setSize(wid, het);
		setVisible(true);
		this.setDefaultCloseOperation(0);
	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}

	public void windowClosing(WindowEvent e) {
		if(JOptionPane.showConfirmDialog(null, "Do you want to  Exit Application ?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			System.exit(0);
		}
		else{
			this.setVisible(true);
		}
	}

	public void windowDeactivated(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowGainedFocus(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowLostFocus(WindowEvent e) {

	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowStateChanged(WindowEvent e) {

	}
}
