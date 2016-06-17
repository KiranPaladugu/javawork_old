import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ResultView extends JPanel {
	private static final long serialVersionUID = 1L;
	public static JTextArea resultView;
	private static boolean isScrollLock;
	private static int lastPosition;
	public static void addToResultView(String text) {
		synchronized (resultView) {
			resultView.append(text);
			if (!isScrollLock) {
				lastPosition = resultView.getDocument().getLength();
				// logView.setCaretPosition(logView.getDocument().getLength());
				// viewScroller.getVerticalScrollBar().setValue(viewScroller.getVerticalScrollBar().getMaximum());
			}
			if (resultView.getDocument().getLength() < lastPosition) {
				lastPosition = 0;
			}
			resultView.setCaretPosition(lastPosition);
		}
	}
	public static void addToResultViewNLine(String text) {
		synchronized (resultView) {
			resultView.append("\n" + text);
			if (!isScrollLock) {
				lastPosition = resultView.getDocument().getLength();
				// logView.setCaretPosition(logView.getDocument().getLength());
				// viewScroller.getVerticalScrollBar().setValue(viewScroller.getVerticalScrollBar().getMaximum());
			}
			if (resultView.getDocument().getLength() < lastPosition) {
				lastPosition = 0;
			}
			resultView.setCaretPosition(lastPosition);
		}
	}
	private JPopupMenu popup;
	private JMenuItem clear;

	private JCheckBoxMenuItem scrolLock;

	private JScrollPane resultScroller;

	public ResultView() {
		initPopup();
		resultView = new JTextArea();
		resultView.add(popup);
		resultView.setEditable(false);
		resultView.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		resultScroller = new JScrollPane(resultView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		resultScroller.setBorder(BorderFactory.createRaisedBevelBorder());
		resultScroller.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		resultView.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				resultScroller.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
			}

			public void focusLost(FocusEvent e) {
				resultScroller.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
			}
		});
		setLayout(new BorderLayout());
		add(resultScroller, BorderLayout.CENTER);
	}

	private void clearOperation() {
		synchronized (resultView) {
			resultView.setText("");
		}
	}

	private void initPopup() {
		popup = new JPopupMenu();
		clear = new JMenuItem("Clear..");
		clear.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("clear.png")));
		clear.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				clearOperation();
			}
		});
		scrolLock = new JCheckBoxMenuItem("LockScroll..");
		scrolLock.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("lock.png")));
		scrolLock.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				setLock(!item.getState());
			}
		});
		popup.add(clear);
		popup.addSeparator();
		popup.add(scrolLock);
	}

	private void setLock(boolean flag) {
		isScrollLock = flag;
	}
}
