package swings;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class JNote implements MouseListener, KeyListener {
	public static void main(String[] args) {
		new JNote();
	}
	JNoteFrame frame;
	JMenuBar menu;
	JTextPane textPane, textPane2;
	JScrollPane scrollPane, scrollPane2;
	JMenu file, edit, format, view, help;
	JMenuItem file_new, file_open, file_exit, file_save, file_saveAs, file_print;
	JMenuItem edit_undo, edit_cut, edit_copy, edit_paste, edit_delete, edit_find, edit_findNext, edit_replace, edit_goto,
			edit_selectAll, edit_time;
	JMenuItem format_font;
	JCheckBoxMenuItem view_statusbar, format_wordWrap;
	JMenuItem view_compare, view_noramlView;
	JMenuItem help_helpContents, help_about;
	JMenuItem undo, cut, copy, paste, selectAll;
	Container c;
	JLabel line1, line2;
	JPopupMenu popup;
	StatusBar statusBar;
	boolean status = true;

	boolean wrap = true;

	public JNote() {
		frame = new JNoteFrame();

		statusBar = new StatusBar();
		/**
		 * Menu Build
		 */

		menu = new JMenuBar();
		file = new JMenu("File");
		file.setMnemonic('F');

		file_new = new JMenuItem("New");
		file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		file_open = new JMenuItem("Open");
		file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		file_exit = new JMenuItem("Exit");
		file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
		file_save = new JMenuItem("Save");
		file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		file_saveAs = new JMenuItem("SaveAs");
		file_saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		file_print = new JMenuItem("Print");
		file_print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));

		file.add(file_new);
		file.add(file_open);
		file.addSeparator();
		file.add(file_save);
		file.add(file_saveAs);
		file.addSeparator();
		file.add(file_print);
		file.addSeparator();
		file.add(file_exit);

		edit = new JMenu("Edit");
		edit.setMnemonic('E');

		edit_undo = new JMenuItem("Undo");
		edit_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		edit_cut = new JMenuItem("Cut");
		edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		edit_copy = new JMenuItem("Copy");
		edit_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
		edit_paste = new JMenuItem("Paste");
		edit_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
		edit_delete = new JMenuItem("Delete");
		edit_delete.setAccelerator(KeyStroke.getKeyStroke("Del"));
		// edit_delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_DOWN_MASK));
		edit_find = new JMenuItem("Find");
		edit_find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
		edit_findNext = new JMenuItem("FindNext");
		edit_replace = new JMenuItem("Repalce");
		edit_replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
		edit_goto = new JMenuItem("Goto");
		edit_selectAll = new JMenuItem("SelectAll");
		edit_selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		edit_time = new JMenuItem("Time/Date");
		edit_time.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));
		edit_time.addMouseListener(this);

		edit.add(edit_undo);
		edit.addSeparator();
		edit.add(edit_cut);
		edit.add(edit_copy);
		edit.add(edit_paste);
		edit.add(edit_delete);
		edit.addSeparator();
		edit.add(edit_find);
		edit.add(edit_findNext);
		edit.addSeparator();
		edit.add(edit_replace);
		edit.addSeparator();
		edit.add(edit_goto);
		edit.add(edit_selectAll);
		edit.addSeparator();
		edit.add(edit_time);

		format = new JMenu("Format");
		format.setMnemonic('o');

		format_wordWrap = new JCheckBoxMenuItem("WordWrap");
		format_wordWrap.addMouseListener(this);
		format_font = new JMenuItem("Font..");
		format_font.addMouseListener(this);

		format.add(format_wordWrap);
		format.addSeparator();
		format.add(format_font);

		view = new JMenu("View");
		view.setMnemonic('V');

		view_statusbar = new JCheckBoxMenuItem("StatusBar");
		view_statusbar.addMouseListener(this);
		view_compare = new JMenuItem("Compare..");
		view_noramlView = new JMenuItem("Normal View ..");
		view_noramlView.setEnabled(false);

		view.add(view_statusbar);
		view.addSeparator();
		view.add(view_compare);
		view.add(view_noramlView);

		help = new JMenu("Help");
		help.setMnemonic('H');

		help_helpContents = new JMenuItem("Help");
		help_about = new JMenuItem("About.");
		help.add(help_helpContents);
		help.addSeparator();
		help.add(help_about);
		menu.add(file);
		menu.add(edit);
		menu.add(format);
		menu.add(view);
		menu.add(help);

		/**
		 * TextArea Build
		 */
		textPane = new JTextPane();
		textPane.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				statusBar.setPosition(textPane.getCaretPosition());
				statusBar.setLine(1);
			}
		});
		textPane.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				evaluateLock();
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		textPane2 = new JTextPane();
		scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setLayout(new ScrollPaneLayout());
		scrollPane2 = new JScrollPane(textPane2);

		/**
		 * Context Menu Build
		 */
		popup = new JPopupMenu();
		undo = new JMenuItem("undo");
		cut = new JMenuItem("cut");
		copy = new JMenuItem("copy");
		paste = new JMenuItem("paste");
		selectAll = new JMenuItem("selectAll");
		popup.add(undo);
		popup.addSeparator();
		popup.add(cut);
		popup.add(copy);
		popup.add(paste);
		popup.add(paste);
		popup.addSeparator();
		popup.add(selectAll);
		textPane.add(popup);
		/**
		 * Layout build
		 */
		c = frame.getContentPane();
		// c.setLayout(new BorderLayout());
		// scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		// statusBar.setBorder(BorderFactory.createRaisedBevelBorder());
		c.add(scrollPane, BorderLayout.CENTER);
		// c.add(scrollPane2,BorderLayout.CENTER);
		c.add(statusBar, BorderLayout.SOUTH);

		/**
		 * Add listeners
		 */
		textPane.addMouseListener(this);

		/**
		 * frame view build.
		 */
		frame.setJMenuBar(menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int wid = 600;
		int het = 500;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - wid) / 2;
		int y = (dim.height - het) / 2;
		frame.setBounds(x, y, wid, het);
		frame.setSize(wid, het);
		evaluateLock();
		frame.setVisible(true);
	}

	private void evaluateLock() {
		if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
			statusBar.setCaps(true);
		} else
			statusBar.setCaps(false);
		if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK)) {
			statusBar.setNum(true);
		} else
			statusBar.setNum(false);
		if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_SCROLL_LOCK)) {
			statusBar.setScroll(true);
		} else
			statusBar.setScroll(false);

	}

	public void keyPressed(KeyEvent e) {
		evaluateLock();

	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource().equals(edit_time)) {
			this.setTime();
		}

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);

	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
		if (e.getSource().equals(format_wordWrap)) {
			JOptionPane.showMessageDialog(null, "Cannot Modify property Word wrap !!");
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		if (e.getSource().equals(view_statusbar)) {
			this.statusBar.setVisible(this.status);
			status = (!status);
			return;
		}
		if (e.getSource().equals(edit_time)) {
			this.setTime();
		}
		if (e.getSource().equals(format_font)) {
			FontStyle fs = FontDialog.showFontChooserDialog(null);
			if (fs != null)
				textPane.setFont(new Font(fs.getFontName(), fs.getStyle(), fs.getSize()));
		}
	}

	private void setTime() {
		String msg = this.textPane.getText();
		msg += new Date().toString();
		this.textPane.setText(msg);
		return;
	}
}
