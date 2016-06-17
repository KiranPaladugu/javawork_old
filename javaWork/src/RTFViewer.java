/* RTFViewer.java
 * March 9, 2003
 */

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * Constructs an RTF editor kit to display the contents of an RTF document in a
 * JEditorPane.
 * 
 * @author: Charles Bell
 * @version March 8, 2003
 */
public class RTFViewer extends JFrame {

	/** For the rtf file filter. */
	class RTFFileNameFilter extends javax.swing.filechooser.FileFilter {

		public boolean accept(File file) {
			boolean status = false;
			String filename = file.getName().toLowerCase();
			return (filename.indexOf("rtf") > 0);
		}

		public String getDescription() {
			return "RTF file";
		}
	}
	/** Runs as application. */
	public static void main(String[] args) {
		if (args.length == 0) {
			new RTFViewer();
		} else {
			new RTFViewer(args[0]);
		}
	}
	private RTFEditorKit kit;

	private File file;

	private JEditorPane editor;

	/** Default constructor. */
	public RTFViewer() {
		super("RTF Viewer");
		init();
	}

	/** Constructor using file object as input. */
	public RTFViewer(File file) {
		super("RTF Viewer: " + file.getName());
		this.file = file;
		init();
	}

	/** Constructor using filename string object as input. */
	public RTFViewer(String fileName) {
		super("RTF Viewer: " + fileName);
		this.file = new File(fileName);
		init();
	}

	/** Control exit. */

	private void exit() {
		System.exit(0);
	}

	/** Uses a JFileDialog to retrieve a file from the user. */
	private File getFile() {
		File f = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new RTFFileNameFilter());
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			f = fileChooser.getSelectedFile();
		}
		return f;
	}

	/**
	 * Creates a default document to hold the contents of the RTF file, and
	 * displays its contents in a JEditorPane.
	 */
	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			kit = new RTFEditorKit();
			editor = new JEditorPane();
			editor.setEditable(false);
			kit.install(editor);
			Document doc = kit.createDefaultDocument();
			if (file == null) {
				file = getFile();
				if (file == null) {
					exit();
				}
			}
			setTitle("RTF Viewer: " + file.getName());
			FileInputStream fis = new FileInputStream(file);
			kit.read(fis, doc, 0);
			editor.setText(doc.getText(0, doc.getLength()));
			setSize(Toolkit.getDefaultToolkit().getScreenSize());
			getContentPane().add(new JScrollPane(editor), BorderLayout.CENTER);
			show();
		} catch (BadLocationException ble) {
			System.err.println("BadLocationException: " + ble.getMessage());
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

}