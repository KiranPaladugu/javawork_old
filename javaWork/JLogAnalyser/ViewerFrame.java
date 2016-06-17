import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import reg.Register;

public class ViewerFrame extends JFrame implements WindowListener, WindowStateListener, WindowFocusListener {
	private static final long serialVersionUID = 1L;
	private Container c;
	private ViewerPanel viewer;
	private MenuView menu;
	boolean stateFindVis = false;
	boolean focFindVis = false;

	public ViewerFrame() {
		super();
		init();
		this.setTitle("Log Viewer");
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
		Register.register(ViewPanel.class, viewer);
		menu = new MenuView(viewer.getView());
		Register.register(MenuView.class, menu);
		c.add(viewer, BorderLayout.CENTER);
		this.addWindowFocusListener(this);
		this.addWindowListener(this);
		this.addWindowStateListener(this);
		this.setJMenuBar(menu);
		Image icon = Toolkit.getDefaultToolkit().getImage(ViewerFrame.class.getResource("2eye.png"));
		this.setIconImage(icon);
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
		FindHandler finder = (FindHandler) Register.getObject(FindHandler.class);
		boolean isVisible = false;
		if (finder != null) {
			isVisible = finder.isVisible();
			finder.setVisible(false);
		}
		if (JOptionPane.showConfirmDialog(null, "Do you want to  Exit Application ?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else {
			this.setVisible(true);
			if (finder != null) {
				finder.setVisible(isVisible);				
			}
		}
	}

	public void windowDeactivated(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {
		FindHandler finder = (FindHandler) Register.getObject(FindHandler.class);
		if (finder != null) {
			if (stateFindVis)
				finder.setVisible(stateFindVis);
		}
		stateFindVis = false;
	}

	public void windowGainedFocus(WindowEvent e) {
		FindHandler finder = (FindHandler) Register.getObject(FindHandler.class);
		if (finder != null) {
			if (focFindVis)
				finder.setVisible(focFindVis);
		}
		focFindVis = false;
	}

	public void windowIconified(WindowEvent e) {
		FindHandler finder = (FindHandler) Register.getObject(FindHandler.class);
		if (finder != null) {
			stateFindVis = finder.isVisible();
			if (stateFindVis) {
				finder.setVisible(false);
			}
		}
	}

	public void windowLostFocus(WindowEvent e) {
		Window otherWin = e.getOppositeWindow();
		FindHandler finder = (FindHandler) Register.getObject(FindHandler.class);
		if (finder != null) {
			Window win = finder;			
			if (!win.equals(otherWin)) {
				if (focFindVis) {
					finder.setVisible(false);
				}
			}
			focFindVis = finder.isVisible();
		}
	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowStateChanged(WindowEvent e) {

	}
}
