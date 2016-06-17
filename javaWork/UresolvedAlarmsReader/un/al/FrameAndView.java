package un.al;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class FrameAndView {
	private String frameTitle = "UNResolved_Alarms";
	private JFrame frame;
	private JPopupMenu popup;
	private JTextArea textArea;
	private JPanel framePanel;
	private JScrollPane scrollPane;
	private JMenu file, view, options, help, file_saveAs;
	private JMenuItem file_open, file_save, file_exit;
	private JMenuItem view_font, saveAs_text, saveAs_Excel;
	private JCheckBoxMenuItem view_wrap;
	private JMenuBar menuBar;
	private File currentFile;
	private boolean save = false;

	public FrameAndView() {
		init();
	}

	private void init() {
		initTheme();
		initFrame();
		initMenuBar();
		initTextPane();
		initFinalFrame();
	}

	private void initTheme() {
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initTextPane() {
		textArea = new JTextArea();
		Font f = new Font("Courier New", Font.PLAIN, 15);
		textArea.setFont(f);
		textArea.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				if (!save) {
					frame.setTitle(frameTitle + " * ");
					save = true;
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
			}
		});
		scrollPane = new JScrollPane(textArea);
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridwidth = GridBagConstraints.REMAINDER;
		gc.insets = new Insets(1, 1, 1, 1);
		gc.gridheight = 30;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		framePanel.add(scrollPane, gc);
		initPopup();
		textArea.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void initPopup() {
		popup = new JPopupMenu();
		JMenuItem parseNobehaviour = new JMenuItem("parseNobehaviour");
		parseNobehaviour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentFile != null) {
					parseUnresolvedAlrms(currentFile, 1);
				}
			}
		});
		JMenuItem parseAIDs = new JMenuItem("parseAIDs");
		parseAIDs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentFile != null) {
					parseUnresolvedAlrms(currentFile, 2);
				}
			}
		});
		JMenuItem parseAll = new JMenuItem("ParseAll");
		parseAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentFile != null) {
					parseUnresolvedAlrms(currentFile, 0);
				}
			}
		});
		popup.add(parseNobehaviour);
		popup.add(parseAIDs);
		popup.add(parseAll);
		textArea.add(popup);
	}

	private class AlarmDetails {
		String alarm = "";
		String context = "";
		String signal = "";
		String tpType = "";
		String pcId = "";
		String pc = "";
		String cardType = "";
		String alarmDetail = "";
		String connType = "";
		String eventType = "";
		String alarmID = "";
		String type = "";
		String syntax="";
		String neName="";
		String neType="";

		public String toString() {
			String msg = "";
			msg = 	alarm + "\t" 
					+ context + "\t"
					+ pc + "\t" 
					+ pcId + "\t"
					+ alarmDetail+"\t"
					+ signal + "\t" 
					+ alarmID + "\t"
					+ cardType + "\t" 
					+ tpType + "\t" 
					+ "\t"+ "\t"+ "\t"+ "\t"
//					+ syntax +"\t"
					+ type + "\t" 
					+ neName +"\t"
					+neType +"\t"
					+ connType + "\t" 
					+ eventType+ "\t" 
					;
			return msg;
		}
	}

	private void parseUnresolvedAlrms(File file, int option) {
		textArea.setText("");
		currentFile = null;
		save = true;
		NeDetailPrinter printer = new NeDetailPrinter();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			Vector<AlarmDetails> v = new Vector<AlarmDetails>();
			addHeader(v);
			String msg = null;
			boolean flag = false;
			boolean newOne = false;
			AlarmDetails alr = new AlarmDetails();
			while ((msg = br.readLine()) != null) {
				if (msg.startsWith("> No behaviours for ME")) {
					if ((option == 1) || (option == 0)) {
						flag = true;
					} else {
						flag = false;
					}
					newOne = false;
					NeDetail detail = printer.getNeDetails(msg);
					alr.neName = detail.getNeName();
					alr.neType = detail.getNeType();
				} else if (msg.startsWith("> For alarm event on ME")) {
					if ((option == 2) || (option == 0)) {
						flag = true;
					} else {
						flag = false;
					}
					NeDetail detail = printer.getNeDetails(msg);
					alr.neName = detail.getNeName();
					alr.neType = detail.getNeType();
					newOne = false;
				} else if (msg.trim().equals("")) {
					newOne = true;
				}
				if (flag && !newOne) {
					if (msg.contains("> No behaviours for ME")) {
						if ((option == 1) || (option == 0)) {
							alr.alarm = msg;
							alr.type = "No behaviour";
						}
					} else if (msg.contains("> For alarm event on ME")) {
						if ((option == 2) || (option == 0)) {
							alr.alarm = msg;
							alr.type = "AID";
						}
					} else if (msg.contains("X36AlContext")) {
						String s = getString(msg, true);
						alr.context = s.trim();
					} else if (msg.contains("X36AlSignalType")) {
						alr.signal = getString(msg, true).trim();
					} else if (msg.contains("X36TpType")) {
						alr.tpType = getString(msg, false).trim();
					} else if (msg.contains("X36AlConnType")) {
						alr.connType = getString(msg, true).trim();
					} else if (msg.contains("alEventType")) {
						alr.eventType = getString(msg, true).trim();
					} else if (msg.contains("alarmId")) {
						alr.alarmID = getString(msg, true).trim();
					} else if (msg.contains("alProbableCause")) {
						alr.pcId = getString(msg, false).trim();
					} else if (msg.contains("alProbableStr")) {
						alr.pc = getString(msg, false).trim();
					} else if (msg.contains("cardDetailStr")) {
						alr.cardType = getString(msg, true).trim();
					} else if (msg.contains("alDetailStr")) {
						alr.alarmDetail = getString(msg, false).trim();
					}
					msg = removeLogSymbol(msg);
					alr.syntax.trim();
					if(alr.syntax.length() == 0){
						alr.syntax+=msg;
					}else{
						alr.syntax+="\n"+msg;
					}
					
				} else if (newOne) {
					addCommentsToSyntax(alr.syntax);
					if (!alr.toString().trim().equals("")) {
						v.add(alr);
					}
					alr = new AlarmDetails();
				}
			}
			for (int i = 0; i < v.size(); i++) {
				textArea.append(v.get(i).toString() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addHeader(Vector<AlarmDetails> v) {
		AlarmDetails a = new AlarmDetails();
		a.type = "Type";
		a.alarm = "Alarm";
		a.context = "Context";
		a.signal = "SignalType";
		a.tpType = "TpType";
		a.connType = "ConnType";
		a.eventType = "EventType";
		a.alarmID = "AlarmID";
		a.pcId = "ProbableCauseID";
		a.pc = "ProbableCause";
		a.cardType = "CardType";
		a.alarmDetail = "Alarmname";
		a.syntax = "Syntax";
		a.neName = "Ne Name";
		a.neType = "Ne Typ";
		v.add(a);
	}

	private void addCommentsToSyntax(String syntax) {
		syntax = "\" "+syntax +"\"";		
	}

	private void addToSyntax(String syntax, String msg) {
		
	}

	private String removeLogSymbol(String msg) {
		String str ="";
		msg = msg.trim();
		if(msg.startsWith(">")){
			str = msg.substring(1);
		}
		return str;
	}

	private String getString(String msg, boolean flag) {
		int x = msg.lastIndexOf(':');
		if (flag) {
			return getNormalString(msg.substring(x + 1, msg.length()));
		} else {
			return msg.substring(x + 1, msg.length());
		}
	}

	private String getNormalString(String msg) {
		String str = "";
		boolean skip = false;
		for (int i = 0; i < msg.length(); i++) {
			char ch = msg.charAt(i);
			if ((ch == '\'')) {
			} else if (ch == '[') {
				skip = true;
			} else if (ch == ']') {
				skip = false;
			} else if (skip) {
			} else {
				str += ch;
			}
		}
		return str;
	}

	private void initFinalFrame() {
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	private void initFrame() {
		frame = new JFrame(frameTitle);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		framePanel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(framePanel, BorderLayout.CENTER);
		frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				performExitOperation(true);
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}
		});
	}

	private void initMenuBar() {
		menuBar = new JMenuBar();
		initMenu();
		frame.setJMenuBar(menuBar);
	}

	private void initMenu() {
		initFileMenu();
		initViewMenu();
		initOptionsMenu();
		initHelpMenu();
		menuBar.add(file);
		menuBar.add(view);
		menuBar.add(options);
		menuBar.add(help);
	}

	private void initHelpMenu() {
		help = new JMenu("Help");
	}

	private void initOptionsMenu() {
		options = new JMenu("Options");
		JMenuItem parseNobehaviour = new JMenuItem("parseNobehaviour");
		parseNobehaviour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentFile != null) {
					parseUnresolvedAlrms(currentFile, 1);
				}
			}
		});
		JMenuItem parseAIDs = new JMenuItem("parseAIDs");
		parseAIDs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentFile != null) {
					parseUnresolvedAlrms(currentFile, 2);
				}
			}
		});
		JMenuItem parseAll = new JMenuItem("ParseAll");
		parseAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentFile != null) {
					parseUnresolvedAlrms(currentFile, 0);
				}
			}
		});
		options.add(parseNobehaviour);
		options.add(parseAIDs);
		options.add(parseAll);
	}

	private void initViewMenu() {
		view = new JMenu("View");
		view_wrap = new JCheckBoxMenuItem("Word wrap");
		view_wrap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setWrapStyleWord(view_wrap.isSelected());
			}
		});
		view_font = new JMenuItem("Font..");
		view_font.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performFontChangeOperation();
			}
		});
		view.add(view_wrap);
		view.addSeparator();
		view.add(view_font);
	}

	private void performFontChangeOperation() {
		Font font = FontHelper.showFontDialog();
		System.out.println(font);
		textArea.setFont(font);
	}

	private static class FontHelper {
		public static Font showFontDialog() {
			Font font = null;
			String selection = JOptionPane.showInputDialog(null, "Type Font Name..", "Courier New");
			font = new Font(selection, Font.PLAIN, 15);
			return font;
		}
	}

	private void initFileMenu() {
		file = new JMenu("File");
		file_open = new JMenuItem("Open");
		file_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performOpenOperation();
			}
		});
		file_save = new JMenuItem("Save");
		file_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performSaveOperation();
			}
		});
		file_exit = new JMenuItem("Exit");
		file_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performExitOperation(false);
			}
		});
		file_saveAs = new JMenu("Save As");
		saveAs_text = new JMenuItem("TextFile");
		saveAs_Excel = new JMenuItem("ExcelFile");
		saveAs_Excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performExcelSave();
			}
		});
		saveAs_text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performSaveAsOperation();
			}
		});
		file_saveAs.add(saveAs_text);
		file_saveAs.add(saveAs_Excel);
		file.add(file_open);
		file.addSeparator();
		file.add(file_save);
		file.add(file_saveAs);
		file.addSeparator();
		file.add(file_exit);
	}

	private void performExcelSave() {
		JOptionPane.showInputDialog(frame, "EnterNameofFile");
		JOptionPane.showMessageDialog(frame, "ERROR!!!\nFile not Saved.", "ERROR!!", JOptionPane.ERROR_MESSAGE);
	}

	private void performExitOperation(boolean confirm) {
		if (confirm) {
			int option = JOptionPane.showConfirmDialog(frame, "Are you sure to Exit ?", "Confirm..", JOptionPane.YES_NO_OPTION);
			if (option != JOptionPane.YES_OPTION) {
				return;
			}
		}
		if (save) {
			int x = JOptionPane.showConfirmDialog(frame, "File is not saved. \n Do you want to save ?", "Save ??",
					JOptionPane.YES_NO_OPTION);
			if (x == JOptionPane.YES_OPTION) {
				performSaveOperation();
			}
		}
		System.exit(0);
	}

	private void performSaveOperation() {
		if (currentFile == null) {
			performSaveAsOperation();
		} else {
			performSaveOperation(currentFile);
		}
	}

	private void performSaveAsOperation() {
		JFileChooser fileChooser = new JFileChooser();
		int x = fileChooser.showSaveDialog(frame);
		if (x == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			currentFile = file;
			performSaveOperation(currentFile);
		}
	}

	private void performSaveOperation(File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(textArea.getText());
			writer.flush();
			writer.close();
			save = false;
			frame.setTitle(frameTitle + " - " + currentFile.getName());
		} catch (Exception e) {
			System.out.println("File Not saved !!\n" + e);
		}
	}

	@SuppressWarnings("unchecked")
	private void performOpenOperation() {
		File file = new File(".open");
		File current = null;
		if (file.exists()) {
			try {
				FileInputStream in = new FileInputStream(file);
				ObjectInputStream ooin = new ObjectInputStream(in);
				Vector<File> currDir = (Vector<File>) ooin.readObject();
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
			openFileToTextViewer(fileChooser.getSelectedFile());
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

	private void openFileToTextViewer(File file) {
		if (file.exists()) {
			if (file.length() > (3 * 1048576)) {
				JOptionPane.showMessageDialog(frame, "File is greater than 3MB !!", "ERROR!!", JOptionPane.ERROR_MESSAGE);
			} else {
				currentFile = file;
				System.out.println(currentFile.length());
				try {
					BufferedReader br = new BufferedReader(new FileReader(currentFile));
					String msg = null;
					textArea.setText("");
					while ((msg = br.readLine()) != null) {
						textArea.append(msg + "\n");
					}
					br.close();
					save = false;
					frame.setTitle(frameTitle + " - " + currentFile.getName());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, "Unable to ReadFile beacuse of \n" + e, "ERROR!!",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(frame, "File Not Found !!", "ERROR!!", JOptionPane.ERROR_MESSAGE);
		}
	}

	class TxtFile extends FileFilter {
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
		}

		public String getDescription() {
			return ".txt";
		}
	}

	class ExcelFile extends FileFilter {
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".xls");
		}

		public String getDescription() {
			return ".xls";
		}
	}
}
