package swings;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class JNoteFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JNoteFrame() {
		super();
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
