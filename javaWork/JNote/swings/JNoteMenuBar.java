package swings;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class JNoteMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	JMenu file, edit, format, view, help;

	public JNoteMenuBar() {
		file = new JMenu("File");
		edit = new JMenu("Edit");
		format = new JMenu("Format");
		view = new JMenu("view");
		help = new JMenu("help");
		add(file);
		add(edit);
		add(format);
		add(view);
		add(help);

	}
}
