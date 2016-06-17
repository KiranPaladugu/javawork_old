import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class PopupMenu extends JPopupMenu implements MouseListener {
	public static String slot1 = null;
	public static String slot2 = null;
	public static String slot3 = null;
	public static String slot4 = null;
	public static String slot5 = null;
	public static String slot6 = null;
	private static final long serialVersionUID = 5253699921475487279L;
	private JMenuItem copy, mark, clear;
	private JMenuItem undo, cut, paste, delete;
	private JCheckBoxMenuItem lockScroll, editable;
	private ViewPanel view;
	private JMenu showOnly;
	private JMenu copyTo;
	private JMenuItem copy_slot1, copy_slot2, copy_slot3, copy_slot4, copy_slot5, copy_slot6;
	private JMenu pasteFrom;
	private JMenuItem paste_slot1, paste_slot2, paste_slot3, paste_slot4, paste_slot5, paste_slot6;
	private JMenuItem pasteAll, selectAll;
	private JCheckBoxMenuItem only_errors, only_exceptions, only_infos, only_debugs, only_warnings, showAll;
	private JMenuItem redo;

	public PopupMenu(ViewPanel view) {
		super();
		this.view = view;
		undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		undo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "undo.png")));
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performUndoOperation();
			}
		});
		redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		redo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "redo.png")));
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performRedoOperation();
			}
		});
		undo.setEnabled(false);
		redo.setEnabled(false);
		cut = new JMenuItem("Cut..");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		cut.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "cut.png")));
		cut.setEnabled(false);
		copy = new JMenuItem("Copy ..");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copy.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "copy.png")));
		copyTo = new JMenu("CopyTo..");
		copy_slot1 = new JMenuItem("SLOT-1");
		copy_slot1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyToSlot1();
			}
		});
		copy_slot2 = new JMenuItem("SLOT-2");
		copy_slot2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyToSlot2();
			}
		});
		copy_slot3 = new JMenuItem("SLOT-3");
		copy_slot3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyToSlot3();
			}
		});
		copy_slot4 = new JMenuItem("SLOT-4");
		copy_slot4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyToSlot4();
			}
		});
		copy_slot5 = new JMenuItem("SLOT-5");
		copy_slot5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyToSlot5();
			}
		});
		copy_slot6 = new JMenuItem("SLOT-6");
		copy_slot6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyToSlot6();
			}
		});
		copyTo.add(copy_slot1);
		copyTo.add(copy_slot2);
		copyTo.add(copy_slot3);
		copyTo.add(copy_slot4);
		copyTo.add(copy_slot5);
		copyTo.add(copy_slot6);
		paste = new JMenuItem("Paste..");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		paste.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "edit-paste.png")));
		pasteFrom = new JMenu("PasteFrom..");
		paste_slot1 = new JMenuItem("SLOT-1");
		paste_slot1.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				pasteFromSlot1();
			}
		});
		paste_slot2 = new JMenuItem("SLOT-2");
		paste_slot2.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				pasteFromSlot2();
			}
		});
		paste_slot3 = new JMenuItem("SLOT-3");
		paste_slot3.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				pasteFromSlot3();
			}
		});
		paste_slot4 = new JMenuItem("SLOT-4");
		paste_slot4.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				pasteFromSlot4();
			}
		});
		paste_slot5 = new JMenuItem("SLOT-5");
		paste_slot5.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				pasteFromSlot5();
			}
		});
		paste_slot6 = new JMenuItem("SLOT-6");
		paste_slot6.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				pasteFromSlot6();
			}
		});
		pasteFrom.add(paste_slot1);
		pasteFrom.add(paste_slot2);
		pasteFrom.add(paste_slot3);
		pasteFrom.add(paste_slot4);
		pasteFrom.add(paste_slot5);
		pasteFrom.add(paste_slot6);
		pasteFrom.setEnabled(false);
		paste.setEnabled(false);
		delete = new JMenuItem("Delete..");
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		delete.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "edit-delete.png")));
		delete.setEnabled(false);
		selectAll = new JMenuItem("Select All");
		selectAll.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				selectAllOperation();
			}
		});
		selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		selectAll.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "view-icon.png")));
		mark = new JMenuItem("Mark here..");
		clear = new JMenuItem("Clear..");
		clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.CTRL_MASK));
		clear.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "clear.png")));
		editable = new JCheckBoxMenuItem("Editable");
		editable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, ActionEvent.CTRL_MASK));
		editable.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "edit.png")));
		editable.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				setEditableOperation(item.getState());
			}
		});
		lockScroll = new JCheckBoxMenuItem("Lock Scroll");
		lockScroll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		lockScroll.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("img" + File.separator + "lock.png")));
		lockScroll.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				setScrollLockOperation(item.getState());
			}
		});

		showOnly = new JMenu("Show Only");
		// group = new ButtonGroup();

		only_debugs = new JCheckBoxMenuItem("Show Debug Statements.");
		only_debugs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSelection();
			}
		});
		only_infos = new JCheckBoxMenuItem("Show info Statements.");
		only_infos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSelection();
			}
		});
		only_warnings = new JCheckBoxMenuItem("Show warning Statements.");
		only_warnings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSelection();
			}
		});
		only_errors = new JCheckBoxMenuItem("Show error Statements.");
		only_errors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSelection();
			}
		});
		;
		only_exceptions = new JCheckBoxMenuItem("Show Exception Statements.");
		only_exceptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSelection();
			}
		});
		only_debugs.setSelected(true);
		only_infos.setSelected(true);
		only_warnings.setSelected(true);
		only_errors.setSelected(true);
		only_exceptions.setSelected(true);

		showOnly.add(only_debugs);
		showOnly.add(only_infos);
		showOnly.add(only_warnings);
		showOnly.add(only_errors);
		showOnly.add(only_exceptions);

		showAll = new JCheckBoxMenuItem("Show All");
		showAll.setSelected(true);
		showAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});
		pasteAll = new JMenuItem("PasteAll");
		pasteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pasteAllOperation();
			}
		});
		clear.addMouseListener(this);
		add(undo);
		add(redo);
		addSeparator();
		add(cut);
		add(copy);
		add(paste);
		add(delete);
		addSeparator();
		add(copyTo);
		add(pasteFrom);
		add(pasteAll);
		pasteAll.setEnabled(false);
		addSeparator();
		add(selectAll);
		addSeparator();
		add(mark);
		add(clear);
		add(lockScroll);
		addSeparator();
		add(showOnly);
		add(showAll);
		addSeparator();
		add(editable);
	}

	private void copyToSlot1() {
		String txt = view.logView.getSelectedText();
		if ((txt != null) && (!txt.equals(""))) {
			slot1 = txt;
		}
		updateSlotInfo();
	}

	private void copyToSlot2() {
		String txt = view.logView.getSelectedText();
		if ((txt != null) && (!txt.equals(""))) {
			slot2 = txt;
		}
		updateSlotInfo();
	}

	private void copyToSlot3() {
		String txt = view.logView.getSelectedText();
		if ((txt != null) && (!txt.equals(""))) {
			slot3 = txt;
		}
		updateSlotInfo();
	}

	private void copyToSlot4() {
		String txt = view.logView.getSelectedText();
		if ((txt != null) && (!txt.equals(""))) {
			slot4 = txt;
		}
		updateSlotInfo();
	}

	private void copyToSlot5() {
		String txt = view.logView.getSelectedText();
		if ((txt != null) && (!txt.equals(""))) {
			slot5 = txt;
		}
		updateSlotInfo();
	}

	private void copyToSlot6() {
		String txt = view.logView.getSelectedText();
		if ((txt != null) && (!txt.equals(""))) {
			slot6 = txt;
		}
		updateSlotInfo();
	}
	private void pasteFromSlot1(){
		if ((slot1 != null) && (!slot1.equals(""))) {
			int pos = view.logView.getCaretPosition();
			view.logView.insert(slot1, pos);
		}
	}
	private void pasteFromSlot2(){
		if ((slot2 != null) && (!slot2.equals(""))) {
			int pos = view.logView.getCaretPosition();
			view.logView.insert(slot2, pos);
		}
	}
	private void pasteFromSlot3(){
		if ((slot3 != null) && (!slot3.equals(""))) {
			int pos = view.logView.getCaretPosition();
			view.logView.insert(slot3, pos);
		}
	}
	private void pasteFromSlot4(){
		if ((slot4 != null) && (!slot4.equals(""))) {
			int pos = view.logView.getCaretPosition();
			view.logView.insert(slot4, pos);
		}
	}
	private void pasteFromSlot5(){
		if ((slot5 != null) && (!slot5.equals(""))) {
			int pos = view.logView.getCaretPosition();
			view.logView.insert(slot5, pos);
		}
	}
	private void pasteFromSlot6(){
		if ((slot6 != null) && (!slot6.equals(""))) {
			int pos = view.logView.getCaretPosition();
			view.logView.insert(slot6, pos);
		}
	}

	private void updateSlotInfo() {
		if (slot1 != null) {
			paste_slot1.setText("<<TEXT-1>>");
			copy_slot1.setText("<<TEXT-1>>");
		} else {
			paste_slot1.setText("SLOT-1");
			copy_slot1.setText("SLOT-1");
		}
		if (slot2 != null) {
			paste_slot2.setText("<<TEXT-2>>");
			copy_slot2.setText("<<TEXT-2>>");
		} else {
			paste_slot2.setText("SLOT-2");
			copy_slot2.setText("SLOT-2");
		}
		if (slot3 != null) {
			paste_slot3.setText("<<TEXT-3>>");
			copy_slot3.setText("<<TEXT-3>>");
		} else {
			paste_slot3.setText("SLOT-3");
			copy_slot3.setText("SLOT-3");
		}
		if (slot4 != null) {
			paste_slot4.setText("<<TEXT-4>>");
			copy_slot4.setText("<<TEXT>-4>");
		} else {
			paste_slot4.setText("SLOT-4");
			copy_slot4.setText("SLOT-4");
		}
		if (slot5 != null) {
			paste_slot5.setText("<<TEXT-5>>");
			copy_slot5.setText("<<TEXT-5>>");
		} else {
			paste_slot5.setText("SLOT-5");
			copy_slot5.setText("SLOT-5");
		}
		if (slot6 != null) {
			paste_slot6.setText("<<TEXT-6>>");
			copy_slot6.setText("<<TEXT-6>>");
		} else {
			paste_slot6.setText("SLOT-6");
			copy_slot6.setText("SLOT-6");
		}

	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		if (e.getSource().equals(clear)) {
			view.setLogText("");
		}
	}

	public void setScrollLockOperation(boolean flag) {
		lockScroll.setSelected(flag);				
		view.setScrollLock(flag);
	}

	public void setEditableOperation(boolean flag) {
		editable.setSelected(flag);
		view.setViewEditable(flag);
		undo.setEnabled(flag);
		redo.setEnabled(flag);
		pasteFrom.setEnabled(flag);
		cut.setEnabled(flag);
		paste.setEnabled(flag);
		delete.setEnabled(flag);
		pasteAll.setEnabled(flag);
	}

	public String getEvaluatedText(String text) {
		if (showAll.isSelected()) {
			return text;
		} else {
			String msg = "";
			ByteArrayInputStream bais = new ByteArrayInputStream(text.getBytes());
			BufferedReader br = new BufferedReader(new InputStreamReader(bais));
			boolean isPrintable = false;
			try {
				String rLine = null;
				while ((rLine = br.readLine()) != null) {
					if ((rLine.startsWith(">")) || (rLine.equalsIgnoreCase(""))) {
						if (isPrintable) {
							msg += rLine + "\n";
						}
					} else {
						if (only_infos.isSelected()) {
							if (rLine.endsWith("INFO")) {
								msg += rLine + "\n";
								isPrintable = true;
							} else {
								isPrintable = false;
							}
						}
						if (only_debugs.isSelected()) {
							if (rLine.endsWith("DEBUG")) {
								msg += rLine + "\n";
								isPrintable = true;
							} else {
								isPrintable = false;
							}
						}
						if (only_warnings.isSelected()) {
							if (rLine.endsWith("WARNING")) {
								msg += rLine + "\n";
								isPrintable = true;
							} else {
								isPrintable = false;
							}
						}
						if (only_errors.isSelected()) {
							if (rLine.endsWith("ERROR")) {
								msg += rLine + "\n";
								isPrintable = true;
							} else {
								isPrintable = false;
							}
						}
						if (only_exceptions.isSelected()) {
							if (rLine.endsWith("EXCEPTION")) {
								msg += rLine + "\n";
								isPrintable = true;
							} else {
								isPrintable = false;
							}
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return msg;
		}
	}

	private void updateSelection() {
		boolean flag = true;
		if ((!only_debugs.isSelected()) || (!only_errors.isSelected()) || (!only_exceptions.isSelected())
				|| (!only_infos.isSelected()) || (!only_warnings.isSelected())) {
			flag = false;
		}
		showAll.setSelected(flag);
	}

	private void selectAll() {
		showAll.setSelected(true);
		only_debugs.setSelected(true);
		only_infos.setSelected(true);
		only_warnings.setSelected(true);
		only_errors.setSelected(true);
		only_exceptions.setSelected(true);
	}

	private void pasteAllOperation() {
		String txt = "";
		if ((slot1 != null) && (!slot1.equals(""))) {
			txt += slot1 + "\n";
		}
		if ((slot2 != null) && (!slot2.equals(""))) {
			txt += slot2 + "\n";
		}
		if ((slot3 != null) && (!slot3.equals(""))) {
			txt += slot3 + "\n";
		}
		if ((slot4 != null) && (!slot4.equals(""))) {
			txt += slot4 + "\n";
		}
		if ((slot5 != null) && (!slot5.equals(""))) {
			txt += slot5 + "\n";
		}
		if ((slot6 != null) && (!slot6.equals(""))) {
			txt += slot6 + "\n";
		}
		if (!txt.equalsIgnoreCase("")) {
			int pos = view.logView.getCaretPosition();
			view.logView.insert(txt, pos);
		}
	}
	private void selectAllOperation(){
		view.logView.setSelectionStart(0);
		view.logView.setSelectionEnd(view.logView.getDocument().getLength()-1);
	}
	public void performUndoOperation(){
		view.performUndoOperation();
		updateUndoRedo();
	}
	public void performRedoOperation(){
		view.performRedoOperation();
		updateUndoRedo();
	}
	public void updateUndoRedo(){
		if(UndoMan.getUndoMan().canUndo()){
			undo.setEnabled(true);
		} else {
			undo.setEnabled(false);
		}
		if(UndoMan.getUndoMan().canRedo()){
			redo.setEnabled(true);
		} else {
			redo.setEnabled(false);
		}
	}
	public void updateUI(){
		super.updateUI();
	}
}
