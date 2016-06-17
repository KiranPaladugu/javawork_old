import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ReaderUI extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1490534575553593763L;
	private JPanel panel;
	private JLabel file, out_lbl;
	private JTextField fileInput, fileOut;
	private JButton browse, read, out_browse;
	private JCheckBox skip, append;
	private int readFileCount;
	private int totalRead;
	private int readDirectoryCount;
	private String msg = "";
	private String savePath = "";
	private JComboBox select;

	public ReaderUI() {
		initView();
		initFrame();
	}

	public JTextField getFileInput() {
		return fileInput;
	}

	public int getReadFileCount() {
		return readFileCount;
	}

	public int getTotalRead() {
		return totalRead;
	}

	public int getReadDirectoryCount() {
		return readDirectoryCount;
	}

	public void initFrame() {
		int wid = 500;
		int het = 250;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - wid) / 2;
		int y = (dim.height - het) / 2;
		setBounds(x, y, wid, het);
		setSize(wid, het);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ReaderUI.class.getResource("report_reader.png")));
		setTitle("Report Builder");
	}

	private void openFileDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setDialogTitle("Select folder/file..");
		int option = fileChooser.showOpenDialog(this);
		if (option == 0) {
			chekForDirectory(fileChooser.getSelectedFile());
			fileInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	private void openOutDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Select a folder to save");
		int option = fileChooser.showOpenDialog(this);
		if (option == 0) {
			savePath = fileChooser.getSelectedFile().getAbsolutePath() + File.separator;
			fileOut.setText(savePath);
		}
	}

	private void chekForDirectory(File file) {
		if (file.isDirectory()) {
			String msg = "NOTE : select file is a Direcory\n\tmake sure this directory doesnt\n\tcontain any other cpu files.";
			JOptionPane.showMessageDialog(this, msg);
			String out = fileOut.getText();
			if ((out != null) && (out.trim().equals(""))) {
				savePath = file.getAbsolutePath() + File.separator;
				fileOut.setText(savePath);
			}
		} else {
			int end = file.getAbsolutePath().lastIndexOf(File.separator);
			savePath = file.getAbsolutePath().substring(0, end) + File.separator;
			fileOut.setText(savePath);
		}
	}

	private void performReadOperation() {
		msg = "";
		totalRead = 0;
		readFileCount = 0;
		readDirectoryCount = 0;
		String fileName = fileInput.getText();
		if ((fileName != null) && (!fileName.equals(""))) {
			File file = new File(fileName);
			readDirectoryOrFile(file);
		}
		msg += "\n Total Files :" + totalRead;
		msg += "\n Total Readed Files:" + readFileCount;
		msg += "\n Total Directories Read:" + readDirectoryCount;
		JOptionPane.showMessageDialog(this, msg);
	}

	private void readDirectoryOrFile(File file) {
		if (file.isDirectory()) {
			readDirectoryCount++;
			File files[] = file.listFiles();
			for (File f : files) {
				readDirectoryOrFile(f);
			}
		} else {
			totalRead++;
			if (file.getName().endsWith(".cpu") || file.getName().endsWith(".disk") || file.getName().endsWith(".mem")) {
				readFileCount++;
				System.out.println("Reading File:" + file.getAbsolutePath());
				System.out.println("And writing to :" + savePath);
				CPU_Reader reader = new CPU_Reader(file, savePath, skip.isSelected());
				reader.setAppend(append.isSelected());
				int sel = select.getSelectedIndex();
				if (sel == 2) {
					reader.setSaveMode(CPU_Reader.SAVE_TO_EXCEL_FILE);
				} else if (sel == 1) {
					reader.setSaveMode(CPU_Reader.SAVE_TO_TEXT_FILE);
				} else {
					reader.setSaveMode(CPU_Reader.SAVE_TO_TEXT_AND_EXCEL_FILES);
				}
				msg = reader.startRead();
			}
		}
	}

	public void initView() {
		panel = new JPanel();
		panel.setLayout(null);
		file = new JLabel("File :");
		file.setBounds(50, 50, 30, 25);
		fileInput = new JTextField();
		fileInput.setBounds(85, 50, 250, 25);
		fileOut = new JTextField();
		fileOut.setBounds(85, 80, 250, 25);
		out_lbl = new JLabel("Out : ");
		out_lbl.setBounds(50, 80, 30, 25);
		browse = new JButton("Browse");
		browse.setBounds(340, 50, 100, 25);
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileDialog();
			}
		});
		out_browse = new JButton("Browse");
		out_browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openOutDialog();
			}
		});
		out_browse.setBounds(340, 80, 100, 25);
		skip = new JCheckBox("Contains 2 headers.");
		skip.setBounds(50, 110, 145, 25);
		append = new JCheckBox("Append Mode");
		append.setBounds(200, 110, 120, 25);
		select = new JComboBox();
		select.addItem(new String("Both(*)"));
		select.addItem(new String("TextFile"));
		select.addItem(new String("ExcelFile"));
		select.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (select.getSelectedIndex() == 2) {
					append.setSelected(false);
					append.setEnabled(false);
				} else {
					append.setEnabled(true);
				}
				if (select.getSelectedIndex() == 0) {
					showAppendMessage();
				}
			}
		});
		append.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (select.getSelectedIndex() == 0) {
					showAppendMessage();
				}

			}
		});
		select.setBounds(325, 110, 100, 25);
		read = new JButton("Read & Parse");
		read.setBounds(150, 135, 150, 25);
		read.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performReadOperation();
			}
		});
		panel.add(file);
		panel.add(fileInput);
		panel.add(browse);
		panel.add(out_lbl);
		panel.add(fileOut);
		panel.add(out_browse);
		panel.add(skip);
		panel.add(append);
		panel.add(select);
		panel.add(read);
		panel.setVisible(true);
		add(panel);
	}

	public void showAppendMessage() {
		if (append.isSelected()) {
			JOptionPane.showMessageDialog(this,
					"For Excel files Append is not possible.\n Excel File will be created for each File.");
		}
	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		int x = JOptionPane.showConfirmDialog(this, " Exit Application.\n Are you sure ?", "Exit Report Builder ?",
				JOptionPane.YES_NO_OPTION);
		if (x == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public void windowDeactivated(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowOpened(WindowEvent e) {

	}
}
