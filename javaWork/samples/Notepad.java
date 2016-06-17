import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class Notepad extends JFrame implements ActionListener {
	Container c;
	public JScrollPane sc;
	public JTextArea t;

	private JMenuBar menubar;

	private JMenu file;
	private JMenuItem file_new;
	private JMenuItem file_open;
	private JSeparator file_sep1;
	private JMenuItem file_save;
	private JMenuItem file_save_as;
	private JSeparator file_sep2;
	private JMenuItem file_print;
	private JSeparator file_sep3;
	private JMenuItem file_close;
	private JMenuItem file_exit;

	private JMenu edit;
	private JMenuItem edit_undo;
	private JMenuItem edit_redo;
	private JSeparator edit_sep1;
	private JMenuItem edit_copy;
	private JMenuItem edit_cut;
	private JMenuItem edit_paste;
	private JMenuItem edit_delete;
	private JSeparator edit_sep2;
	private JMenuItem edit_find;
	private JMenuItem edit_find_next;
	private JMenuItem edit_replace;
	private JSeparator edit_sep3;
	private JMenuItem edit_selectall;
	private JMenuItem edit_timedate;

	private JMenu format;
	private JMenuItem format_font;
	private JMenu convert;
	private JMenuItem str2uppr, str2lwr;
	private JCheckBoxMenuItem format_wordwarp;

	private JMenu help;
	private JMenuItem help_about;

	UndoManager undo = new UndoManager();
	UIManager.LookAndFeelInfo lnf[];

	find finder;
	font_chooser fc;
	About abt;

	String path, content;

	public Notepad() {
		super("untitled - Notepad");
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Container c = getContentPane();
		t = new JTextArea("", 5, 5);
		t.setFont(new Font("Verdana", Font.PLAIN, 12));
		sc = new JScrollPane(t, sc.VERTICAL_SCROLLBAR_AS_NEEDED, sc.HORIZONTAL_SCROLLBAR_AS_NEEDED); // adding
		c.add(sc);
		menubar = new JMenuBar();
		file = new JMenu("File");
		file_new = new JMenuItem("New");
		file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		file_new.addActionListener(this);
		file.add(file_new);
		file_open = new JMenuItem("Open");
		file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		file_open.addActionListener(this);
		file.add(file_open);
		file_sep1 = new JSeparator();
		file.add(file_sep1);
		file_save = new JMenuItem("Save");
		file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		file_save.addActionListener(this);
		file.add(file_save);
		file_save_as = new JMenuItem("Save As");
		file_save_as.addActionListener(this);
		file.add(file_save_as);
		file_sep2 = new JSeparator();
		file.add(file_sep2);
		file_print = new JMenuItem("Print");
		file_print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		file_print.addActionListener(this);
		file.add(file_print);
		file_sep3 = new JSeparator();
		file.add(file_sep3);
		file_close = new JMenuItem("Close");
		file_close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK));
		file_close.addActionListener(this);
		file.add(file_close);
		file_exit = new JMenuItem("Exit");
		file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		file_exit.addActionListener(this);
		file.add(file_exit);
		menubar.add(file);

		edit = new JMenu("Edit");
		edit_undo = new JMenuItem("Undo");
		edit_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		edit_undo.addActionListener(this);
		edit.add(edit_undo);
		edit_redo = new JMenuItem("Redo");
		edit_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		edit_redo.addActionListener(this);
		edit.add(edit_redo);
		edit_sep1 = new JSeparator();
		edit.add(edit_sep1);
		edit_copy = new JMenuItem("Copy");
		edit_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		edit_copy.addActionListener(this);
		edit.add(edit_copy);
		edit_cut = new JMenuItem("Cut");
		edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		edit_cut.addActionListener(this);
		edit.add(edit_cut);
		edit_paste = new JMenuItem("Paste");
		edit_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		edit_paste.addActionListener(this);
		edit.add(edit_paste);
		edit_delete = new JMenuItem("Delete");
		edit_delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		edit_delete.addActionListener(this);
		edit.add(edit_delete);
		edit_sep2 = new JSeparator();
		edit.add(edit_sep2);
		edit_find = new JMenuItem("Find");
		edit_find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		edit_find.addActionListener(this);
		edit.add(edit_find);
		edit_find_next = new JMenuItem("Find Next");
		edit_find_next.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		edit_find_next.addActionListener(this);
		edit.add(edit_find_next);
		edit_replace = new JMenuItem("Replace");
		edit_replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		edit_replace.addActionListener(this);
		edit.add(edit_replace);
		edit_sep3 = new JSeparator();
		edit.add(edit_sep3);
		edit_selectall = new JMenuItem("Select All");
		edit_selectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		edit_selectall.addActionListener(this);
		edit.add(edit_selectall);
		edit_timedate = new JMenuItem("Time/Date");
		edit_timedate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		edit_timedate.addActionListener(this);
		edit.add(edit_timedate);
		menubar.add(edit);

		format = new JMenu("Format");
		format_font = new JMenuItem("Font");
		format_font.addActionListener(this);
		format.add(format_font);

		convert = new JMenu("Convert");
		str2uppr = new JMenuItem("To Uppercase...");
		str2uppr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK));
		str2uppr.addActionListener(this);
		convert.add(str2uppr);
		str2lwr = new JMenuItem("To Lowercase...");
		str2lwr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK));
		str2lwr.addActionListener(this);
		convert.add(str2lwr);
		format.add(convert);

		format_wordwarp = new JCheckBoxMenuItem("Word Warp");
		format_wordwarp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		format_wordwarp.addActionListener(this);
		format.add(format_wordwarp);
		menubar.add(format);

		help = new JMenu("Help");
		help_about = new JMenuItem("About");
		help_about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		help_about.addActionListener(this);
		help.add(help_about);
		menubar.add(help);

		c.add(menubar, BorderLayout.NORTH);

		// undo manager
		Document doc = t.getDocument();
		doc.addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent event) {
				undo.addEdit(event.getEdit());
			}
		});

		// find_window
		finder = new find(this);
		finder.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// font chooser
		fc = new font_chooser(this);
		abt = new About(this);

		// set window size
		int w = 400;
		int h = 450;
		setSize(400, 450);
		// set window position
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		setLocation(center.x - w / 2, center.y - h / 2);
		this.setVisible(true);
		path = "";
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == file_new)
			file_new();
		else if (e.getSource() == file_open)
			file_open();
		else if (e.getSource() == file_save)
			file_save();
		else if (e.getSource() == file_save_as)
			file_save_as();
		else if (e.getSource() == file_print)
			file_print();
		else if (e.getSource() == file_close)
			file_close();
		else if (e.getSource() == file_exit)
			file_exit();

		else if (e.getSource() == edit_undo)
			edit_undo();
		else if (e.getSource() == edit_redo)
			edit_redo();
		else if (e.getSource() == edit_cut)
			edit_cut();
		else if (e.getSource() == edit_copy)
			edit_copy();
		else if (e.getSource() == edit_paste)
			edit_paste();
		else if (e.getSource() == edit_delete)
			edit_delete();
		else if (e.getSource() == edit_find)
			edit_find();
		else if (e.getSource() == edit_find_next)
			edit_find_next();
		else if (e.getSource() == edit_replace)
			edit_replace();
		else if (e.getSource() == edit_selectall)
			edit_selectall();
		else if (e.getSource() == edit_timedate)
			edit_timedate();

		else if (e.getSource() == format_font)
			format_font();
		else if (e.getSource() == str2uppr)
			str2uppr();
		else if (e.getSource() == str2lwr)
			str2lwr();
		else if (e.getSource() == format_wordwarp)
			format_wordwarp();

		else if (e.getSource() == help_about)
			help_about();
	}

	public void edit_copy() {
		t.copy();
	}

	public void edit_cut() {
		t.cut();
	}

	public void edit_delete() {
		String temp = t.getText();
		t.setText(temp.substring(0, t.getSelectionStart()) + temp.substring(t.getSelectionEnd()));
	}

	public void edit_find() {
		finder.setVisible(true);
	}

	public void edit_find_next() {
		finder.find_next();
	}

	public void edit_paste() {
		t.paste();
	}

	public void edit_redo() {
		if (undo.canRedo()) {
			try {
				undo.redo();
			} catch (CannotRedoException e) {
			}
		}
	}

	public void edit_replace() {
		finder.setVisible(true);
	}

	public void edit_selectall() {
		t.selectAll();
	}

	public void edit_timedate() {

		try {
			int start = t.getSelectionStart();
			int end = t.getSelectionEnd();
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy h:m a");
			String now = sdf.format(cal.getTime());
			String temp1 = t.getText().substring(0, start);
			String temp2 = t.getText().substring(end);
			t.setText(temp1 + " " + now + " " + temp2);
			t.select(start + 1, start + 1 + now.length());
		} catch (NullPointerException e) {
		}
	}

	public void edit_undo() {
		if (undo.canUndo()) {
			try {
				undo.undo();
			} catch (CannotUndoException e) {
			}
		}
	}

	public void file_close() {
		if (t.getText().equals("") || t.getText().equals(content)) {
			t.setText("");
			path = "";
			setTitle("untitled - Notepad");
		} else {
			int a = JOptionPane.showConfirmDialog(null, "The text has been changed\nDo you want to save the changes?");
			if (a == 0)
				file_save();
			else if (a == 1) {
				t.setText("");
				path = "";
				setTitle("untitled - Notepad");
			} else if (a == 2)
				return;
		}

	}

	public void file_exit() {

		if (t.getText().equals("") || t.getText().equals(content))
			System.exit(0);
		else {
			int b = JOptionPane.showConfirmDialog(null, "The text has been changed.\nDo you want to save the changes?");

			if (b == 0)
				file_save();
			else if (b == 1)
				System.exit(0);
			else if (b == 2)
				return;
		}
	}

	public void file_new() {
		if (t.getText().equals("") || t.getText().equals(content)) {
			t.setText("");
			content = "";
			path = "";
			setTitle("untitled - Notepad");
		} else {
			int a = JOptionPane.showConfirmDialog(null, "The text has been changed\nDo you want to save the changes?");
			if (a == 0)
				file_save();
			else if (a == 1) {
				t.setText("");
				path = "";
				setTitle("untitled - Notepad");
			} else if (a == 2)
				return;
		}
	}

	public void file_open() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int r = fc.showOpenDialog(this);
		if (r == fc.CANCEL_OPTION)
			return;
		File myfile = fc.getSelectedFile();
		if (myfile == null || myfile.getName().equals("")) {
			JOptionPane.showMessageDialog(this, "Select a file!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			BufferedReader input = new BufferedReader(new FileReader(myfile));
			StringBuffer str = new StringBuffer();
			String line;
			while ((line = input.readLine()) != null)
				// st is declared as string avobe
				str.append(line + "\n");
			t.setText(str.toString());
			content = t.getText();
			path = myfile.toString();
			setTitle(myfile.getName() + " - Notepad");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found: " + e);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO ERROR: " + e);
		}
	}

	public void file_print() {
		PrinterJob printer = PrinterJob.getPrinterJob();
		// printer.setPrintable( this);
		HashPrintRequestAttributeSet printAttr = new HashPrintRequestAttributeSet();
		if (printer.printDialog(printAttr)) // Display print dialog
		{ // If true is returned...
			try {
				printer.print(printAttr); // then print
			} catch (PrinterException e) {
				JOptionPane.showMessageDialog(this, "Failed to print the file: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void file_save() {
		if (path.equals("")) {
			file_save_as();
			return;
		}
		try {
			FileWriter fw = new FileWriter(path);
			fw.write(t.getText());
			content = t.getText();
			fw.close();
		} catch (IOException i) {
			JOptionPane.showMessageDialog(this, "Failed to save the file", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void file_save_as() {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int r = fc.showSaveDialog(this);
		if (r == fc.CANCEL_OPTION)
			return;
		File myfile = fc.getSelectedFile();
		if (myfile == null || myfile.getName().equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter a file name!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (myfile.exists()) {
			r = JOptionPane.showConfirmDialog(this, "A file with same name already exists!\nAre you sure want to overwrite?");
			if (r != 0)
				return;
		}
		try {
			FileWriter fw = new FileWriter(myfile);
			fw.write(t.getText());
			content = t.getText();
			setTitle(myfile.getName() + " - Notepad");
			fw.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Failed to save the file", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void format_font() {
		fc.window.setVisible(true);
	}

	public void format_wordwarp() {
		if (t.getLineWrap() == false)
			t.setLineWrap(true);
		else
			t.setLineWrap(false);
	}

	public void help_about() {
		abt.window.setVisible(true);
	}

	public void str2lwr() {
		try {
			int start = t.getSelectionStart();
			int end = t.getSelectionEnd();
			String temp1 = t.getText().substring(0, start);
			String temp2 = t.getText().substring(end);
			String conv = t.getSelectedText().toLowerCase();
			t.setText(temp1 + conv + temp2);
			t.select(start, end);
		} catch (NullPointerException e) {
		}
	}

	public void str2uppr() {
		try {
			int start = t.getSelectionStart();
			int end = t.getSelectionEnd();
			String temp1 = t.getText().substring(0, start);
			String temp2 = t.getText().substring(end);
			String conv = t.getSelectedText().toUpperCase();
			t.setText(temp1 + conv + temp2);
			t.select(start, end);
		} catch (NullPointerException e) {
		}
	}
}