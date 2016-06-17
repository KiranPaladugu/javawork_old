import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;

public class MenuView extends JMenuBar implements MouseListener, MenuKeyListener, MenuListener {
	class LogFileFilter extends FileFilter {
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".log");
		}

		public String getDescription() {
			return ".log";
		}
	}

	class TxtFileFilter extends FileFilter {
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
		}

		public String getDescription() {
			return ".txt";
		}
	}

	class AllFileFilter extends FileFilter {

		public boolean accept(File f) {
			return true;
		}

		public String getDescription() {
			return "All Files";
		}

	}

	private static final long serialVersionUID = 1L;
	private JMenu file, edit, options, view, help;
	private JMenuItem file_open, file_save, file_saveAs, file_print, file_exit;
	private JMenu file_new;
	private JMenuItem new_file, new_window, new_browser;
	private JMenu file_recent;
	private JMenuItem edit_undo, edit_cut, edit_copy, edit_paste, edit_delete, edit_find, edit_findNext, edit_replace,
			edit_selectAll;
	private JMenuItem options_mark, options_prefernces;
	private JMenuItem help_about;
	private ViewPanel viewPanel;
	private JCheckBoxMenuItem toolBar, wordWrap, statusBar;
	private String currentFile;

	@SuppressWarnings("unused")
	public MenuView(ViewPanel viewPanel) {
		super();
		this.viewPanel = viewPanel;
		file = new JMenu("File");
		file.setMnemonic('F');
		file_new = new JMenu("New");
		file_new.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("file-new.png"))));
		new_file = new JMenuItem("File");
		new_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("notepad");
				} catch (IOException e1) {
					ResultView.addToResultViewNLine(e1.getLocalizedMessage());
				}
			}
		});
		new_window = new JMenuItem("Explorer");
		new_window.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("explorer");
				} catch (IOException e1) {
					ResultView.addToResultViewNLine(e1.getLocalizedMessage());
				}
			}
		});
		new_browser = new JMenuItem("CommandPrompt!");
		new_browser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("cmd");
				} catch (IOException e1) {
					ResultView.addToResultViewNLine(e1.getLocalizedMessage());
				}
			}
		});
		file_new.add(new_file);
		file_new.setMnemonic('N');
		file_new.add(new_window);
		file_new.add(new_browser);
		file_recent = new JMenu("Recently Opened");
		file_recent.getAccessibleContext().setAccessibleDescription(" This contains recently opened files ");
		file_recent.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("recent.png"))));
		Vector<File> vector = RecentStorer.readRecent();
		if (vector != null) {
			int count = 0;
			for (int i = vector.size() - 1; i >= 0; i--) {
				count++;
				// if(count >12)
				// break;
				JMenuItem item = new JMenuItem(vector.get(i).getAbsolutePath());
				item.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						JMenuItem it = (JMenuItem) e.getSource();
						currentFile = it.getText();
						openOperation(currentFile);
					}
				});
				item.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("goto.png"))));
				file_recent.add(item);
			}
		}
		file_open = new JMenuItem("Open..");
		file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		file_open.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("file-open.png"))));
		file_open.setMnemonic('o');
		file_open.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				openOperation();
			}
		});
		file_save = new JMenuItem("Save");
		file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		file_save.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("file-save.png"))));
		file_saveAs = new JMenuItem("SaveAS..");
		file_saveAs.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("file-save-as.png"))));
		file_print = new JMenuItem("Print");
		file_print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
		file_print.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("print.png"))));
		file_exit = new JMenuItem("Exit");
		file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		file_exit.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("exit.png"))));
		file_exit.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				exitOperation();
			}
		});

		file.add(file_new);
		file.add(file_open);
		file.addSeparator();
		file.add(file_recent);
		file.addSeparator();
		file.add(file_save);
		file.add(file_saveAs);
		file.addSeparator();
		file.add(file_print);
		file.addSeparator();
		file.add(file_exit);

		edit = new JMenu("Edit");
		edit.setMnemonic('E');
		edit_undo = new JMenuItem("Undo..");
		edit_undo.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("undo.png"))));
		edit_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		edit_undo.setEnabled(false);
		edit_cut = new JMenuItem("Cut ..");
		edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		edit_cut.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("cut.png"))));
		edit_cut.setEnabled(false);
		edit_copy = new JMenuItem("Copy ..");
		edit_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		edit_copy.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("copy.png"))));
		edit_paste = new JMenuItem("Paste ..");
		edit_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		edit_paste.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("edit-paste.png"))));
		edit_paste.setEnabled(false);
		edit_delete = new JMenuItem("Delete..");
		KeyStroke delKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		edit_delete.setAccelerator(delKeyStroke);
		edit_delete.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("edit-delete.png"))));
		edit_delete.setEnabled(false);
		edit_find = new JMenuItem("Find ..");
		edit_find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		edit_find.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("file-find.png"))));
		edit_find.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				findOperation();
			}
		});
		edit_findNext = new JMenuItem("FindNext");
		edit_findNext.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("find.png"))));
		edit_replace = new JMenuItem("Replace ..");
		edit_replace.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("page-edit.png"))));
		edit_replace.setEnabled(false);
		edit_selectAll = new JMenuItem("SelectAll");
		edit_selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		edit_selectAll.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("view-icon.png"))));
		edit.add(edit_undo);
		edit.addSeparator();
		edit.add(edit_cut);
		edit.add(edit_copy);
		edit.add(edit_paste);
		edit.add(edit_delete);
		edit.addSeparator();
		edit.add(edit_find);
		edit.add(edit_findNext);
		edit.add(edit_replace);
		edit.addSeparator();
		edit.add(edit_selectAll);

		view = new JMenu("View");
		toolBar = new JCheckBoxMenuItem("Tool Bar");
		toolBar.setSelected(true);
		toolBar.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				enableToolbar(toolBar.isSelected());
			}
		});
		wordWrap = new JCheckBoxMenuItem("Word Wrap");
		wordWrap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setWordWrap(wordWrap.isSelected());
			}
		});
		statusBar = new JCheckBoxMenuItem("StatusBar");
		statusBar.setSelected(true);
		statusBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				statusBarOption(statusBar.isSelected());
			}
		});
		view.add(toolBar);
		view.addSeparator();
		view.add(wordWrap);
		view.addSeparator();
		view.add(statusBar);
		view.setMnemonic('V');

		options = new JMenu("Options");
		options.setMnemonic('O');
		options_mark = new JMenuItem("Mark Position..");
		options_prefernces = new JMenuItem("Preferences");
		options_prefernces
				.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("settings.png"))));
		options.add(options_mark);
		options.addSeparator();
		options.add(options_prefernces);

		help = new JMenu("Help");
		help.setMnemonic('H');
		help_about = new JMenuItem("about ..");
		help_about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * String msg="This devolped by *****."; Icon icon = new
				 * ImageIcon
				 * (Toolkit.getDefaultToolkit().getImage(MenuView.class.
				 * getResource("help.png")));
				 * JOptionPane.showMessageDialog(null, msg, "ABOUT JAnalyser",
				 * JOptionPane.INFORMATION_MESSAGE,icon);
				 */
				new AboutJLogView();
			}
		});
		help_about.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(MenuView.class.getResource("help-center.png"))));
		help.add(help_about);

		add(file);
		add(edit);
		add(view);
		add(options);
		add(help);
	}

	public void aboutOpeation() {

	}

	public void copyOperation() {

	}

	private void enableToolbar(boolean selected) {
		ViewerPanel.getToolbar().setVisible(selected);
	}

	protected void exitOperation() {
		System.exit(0);
	}

	public void findNextOperation() {
		viewPanel.showFindDialog();
	}

	public void findOperation() {
		viewPanel.showFindDialog();
	}

	public void markPositionOperation() {

	}

	public void menuCanceled(MenuEvent e) {

	}

	public void menuDeselected(MenuEvent e) {

	}

	public void menuKeyPressed(MenuKeyEvent e) {

	}

	public void menuKeyReleased(MenuKeyEvent e) {

	}

	public void menuKeyTyped(MenuKeyEvent e) {

	}

	public void menuSelected(MenuEvent e) {

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

	}

	public void openOperation() {
		File file = new File(".open");
		File current = null;
		if (file.exists()) {
			try {
				FileInputStream in = new FileInputStream(file);
				ObjectInputStream ooin = new ObjectInputStream(in);
				Vector<File> currDir = extracted(ooin);
				if (currDir.size() > 0) {
					current = currDir.get(0);
				}
				ooin.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		JFileChooser fileChooser = new JFileChooser();
		if (current != null) {
			fileChooser.setCurrentDirectory(current);
		}		
		int x = fileChooser.showDialog(null, "Open");
		File now = fileChooser.getCurrentDirectory();
		if (x == JFileChooser.APPROVE_OPTION) {
			now = fileChooser.getCurrentDirectory();
			ResultView.addToResultViewNLine("Opening File..:" + fileChooser.getSelectedFile().getAbsolutePath());
			if (RecentStorer.storeToRecent(fileChooser.getSelectedFile())) {
				JMenuItem item = new JMenuItem(fileChooser.getSelectedFile().getAbsolutePath());
				file_recent.add(item);
				item.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						JMenuItem it = (JMenuItem) e.getSource();
						currentFile = it.getText();
						openOperation(currentFile);
					}
				});
			}else {
				currentFile = fileChooser.getSelectedFile().getAbsolutePath();
			}
			Tailer.makeview(viewPanel, fileChooser.getSelectedFile());
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream oout = new ObjectOutputStream(out);
			Vector<File> currDir = new Vector<File>();
			currDir.add(now);
			oout.writeObject(currDir);
			oout.close();
			out.close();
		} catch (Exception e) {
		}

	}
	public String getCurrentFile(){
		return currentFile;
	}
	@SuppressWarnings("unchecked")
	private Vector<File> extracted(ObjectInputStream ooin) throws IOException, ClassNotFoundException {
		return (Vector<File>) ooin.readObject();
	}

	public void openOperation(String fileName) {
		ResultView.addToResultViewNLine("Opening File..:" + fileName);
		File file = new File(fileName);
		Tailer.makeview(viewPanel, file);
	}

	public void preferencesOperation() {

	}

	public void printOperation() {

	}

	public void saveAsOperation() {

	}

	public void saveOperation() {

	}

	public void selectAllOperation() {

	}

	private void setWordWrap(boolean flag) {
		if (flag) {
			wordWrap.setSelected(flag);
		}
		viewPanel.setWordWrap(flag);
	}

	private void statusBarOption(boolean flag) {
		ViewerPanel.getStatusBar().setVisible(flag);
	}
}
