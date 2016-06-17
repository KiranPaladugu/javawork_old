import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;

import reg.Register;

public class ViewPanel extends JPanel implements MouseListener, MouseMotionListener, CaretListener {
	private static final long serialVersionUID = 1L;
	JTextArea logView;
	private JScrollPane viewScroller;
	private PopupMenu popup;
	private int capacity = 1048576;
	private int capCount = 2;
	private boolean isScrollLock = false;
	private boolean setEditable = false;
	private int lastPosition = 0;
	private ViewKeyListener viewKeyListener;
	private FindHandler finder;
	private ToolBar toolbar;
	private boolean pauseFlag = false;
	private boolean pauseEnabled = false;
	private String pauseText = "";

	public ViewPanel() {
		logView = new JTextArea();
		logView.requestFocus();
		logView.setEditable(setEditable);
		viewScroller = new JScrollPane(logView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		viewScroller.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		logView.addCaretListener(this);
		popup = new PopupMenu(this);
		logView.add(popup);
		logView.addMouseListener(this);
		logView.addMouseMotionListener(this);
		viewScroller.addMouseListener(this);
		logView.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				viewScroller.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
			}

			public void focusLost(FocusEvent e) {
				viewScroller.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
			}
		});

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridheight = GridBagConstraints.REMAINDER;
		gc.gridwidth = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.05;
		gc.weighty = 1;
		gc.gridx = 1;
		gc.weightx = 1;
		gc.gridwidth = GridBagConstraints.REMAINDER;
		add(viewScroller, gc);
		viewScroller.setWheelScrollingEnabled(true);
		viewScroller.requestFocusInWindow();
		Document doc = logView.getDocument();
		doc.addUndoableEditListener(UndoMan.getUndoMan());
		logView.requestFocusInWindow();
		viewKeyListener = new ViewKeyListener(this);
		logView.addKeyListener(viewKeyListener);
	}

	public synchronized void appendToLogText(String text) {
		text = getAppendableText(text);
		synchronized (logView) {
			Document doc = logView.getDocument();
			doc.removeUndoableEditListener(UndoMan.getUndoMan());
			int docLen = doc.getLength();
			if (docLen <= (capacity * capCount)) {
				logView.append(text);
			} else {
				try {
					if (pauseEnabled) {
						if (!pauseFlag) {
							toolbar = (ToolBar) Register.getObject(ToolBar.class);
							if (toolbar != null) {
								toolbar.setPause();
								pauseText = text;
								pauseFlag = true;
							}
						} else {
							logView.getAccessibleContext().getAccessibleEditableText().setTextContents("");
							logView.append(pauseText);
							logView.append(text);
							pauseFlag = false;
						}
					} else {
						logView.getAccessibleContext().getAccessibleEditableText().setTextContents("");
						logView.append(text);
					}
				} catch (Exception e) {
					ResultView.addToResultViewNLine("" + e);
				}
				// ResultView.addToResultViewNLine(""+capCount+"M limit Reached  .... Limit breach.");
			}
			if (!isScrollLock) {
				lastPosition = logView.getDocument().getLength();
			}
			if (logView.getDocument().getLength() < lastPosition) {
				lastPosition = 0;
			}
			if (!isScrollLock)
				logView.setCaretPosition(lastPosition);
			doc.addUndoableEditListener(UndoMan.getUndoMan());
		}
	}

	private String getAppendableText(String text) {
		return popup.getEvaluatedText(text);
	}

	public void caretUpdate(CaretEvent e) {

	}

	public String getLogText() {
		return logView.getText();
	}

	public JTextArea getLogViewComponent() {
		return logView;
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		if (popup.isCursorSet()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	public void setLogText(String text) {
		logView.setText(text);
	}

	public void setScrollLock(boolean flag) {
		isScrollLock = flag;
	}

	public void setViewEditable(boolean flag) {
		logView.setEditable(flag);

	}

	public void performCopyOperation() {
		logView.copy();
	}

	public void performCutOperation() {
		logView.cut();
	}

	public void performPasteOperation() {
		logView.paste();
	}

	public void performDeleteOpeation(int startIndex, int endIndex) {
		logView.getAccessibleContext().getAccessibleEditableText().delete(startIndex, endIndex);
	}

	public void performDeleteOperation() {
		logView.getAccessibleContext().getAccessibleEditableText().delete(logView.getSelectionStart(), logView.getSelectionEnd());
	}

	public void performClearOperation() {
		logView.getAccessibleContext().getAccessibleEditableText().setTextContents("");
	}

	public void performUndoOperation() {
		if (UndoMan.getUndoMan().canUndo()) {
			try {
				UndoMan.getUndoMan().undo();
			} catch (Exception e) {
			}
		}

	}

	public void performRedoOperation() {
		if (UndoMan.getUndoMan().canRedo()) {
			try {
				UndoMan.getUndoMan().redo();
			} catch (Exception e) {
			}
		}
	}

	public void setWordWrap(boolean flag) {
		this.logView.setWrapStyleWord(flag);
		this.logView.setLineWrap(flag);
	}

	public PopupMenu getPopup() {
		return popup;
	}

	public void showFindDialog() {
		if (finder == null) {
			finder = (FindHandler) Register.getObject(FindHandler.class);
			if (finder == null) {
				finder = new FindHandler(this);
				Register.register(finder);
			}
		}
		finder.setVisible(true);
	}

	public void setPauseEnabled(boolean enable) {
		pauseEnabled = enable;
	}
}
