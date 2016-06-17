package swings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FontDialog extends JDialog implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private static FontStyle font;
	public static void main(String[] args) {
		System.out.println("Now starting .... Dialog");
		FontStyle fs = showFontChooserDialog(null);
		System.out.println("Dialog Closed....");
		if (fs != null)
			JOptionPane.showMessageDialog(null, fs.toString());
		else
			JOptionPane.showMessageDialog(null, "NULL");
	}
	/**
	 * 
	 * @param parent
	 * @return
	 */
	public static FontStyle showFontChooserDialog(Frame parent) {
		FontDialog fd = new FontDialog(parent);
		fd.setVisible(true);
		return font;
	}
	public static FontStyle showFontChoosetDialog() {
		return showFontChooserDialog(null);
	}
	private JList fontList;
	private JList sizeChooser;
	private String fontNames[];
	private Integer defaultSizes[];
	private JList fontStyle;
	private JLabel sampleView, font_label, style_label, size_label;
	private JScrollPane fontScroller, sizeScroller;
	private String sample = "AaBbCcDdEeFfGg";
	private JButton ok, cancel;

	private JTextField fontName, styleName, sizeValue;

	private JPanel samplePanel, panel;

	private String styles[] = { "Regular", "Bold", "Italic", "Bold Italic" };

	private FontDialog(Frame parent) {
		super(parent, true);
		this.setTitle("Font chooser");
		String windowsTheme = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(windowsTheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		font = null;
		panel = new JPanel();

		fontName = new JTextField();
		styleName = new JTextField();
		sizeValue = new JTextField();

		fontName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 27)
					escapePressed();
				if ((e.getKeyCode() >= 65) && (e.getKeyCode() <= 90)) {
					int i = searchIndex(fontName.getText(), fontNames);
					fontList.setSelectedIndex(i);
					fontList.ensureIndexIsVisible(i);
				} else if ((e.getKeyCode() == 8) || (e.getKeyChar() == ' ') || (e.getKeyCode() == 16)) {
					System.out.println("in..");
				} else if (e.getKeyCode() == 10) {
					fontName.setText(fontNames[fontList.getSelectedIndex()]);
					repaintView();
				} else {
					fontName.setText("");
				}
			}
		});
		styleName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 27)
					escapePressed();
				if ((e.getKeyCode() >= 65) && (e.getKeyCode() <= 90)) {
					int i = searchIndex(styleName.getText(), styles);
					fontStyle.setSelectedIndex(i);
					fontStyle.ensureIndexIsVisible(i);
				} else if ((e.getKeyCode() == 8) || (e.getKeyChar() == ' ') || (e.getKeyCode() == 16)) {
					System.out.println("in..");
				} else if (e.getKeyCode() == 10) {
					styleName.setText(styles[fontStyle.getSelectedIndex()]);
					repaintView();
				} else {
					styleName.setText("");
				}
			}
		});
		sizeValue.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 27)
					escapePressed();
				else if ((e.getKeyCode() >= 96) && (e.getKeyCode() <= 105)) {
					int n = 10;
					try {
						n = Integer.parseInt(sizeValue.getText());
					} catch (Exception ex) {
						sizeValue.setText("");
					}
					if (n < 101) {
						sizeChooser.setSelectedIndex(n - 1);
						sizeChooser.ensureIndexIsVisible(n - 1);
					}
				} else if ((e.getKeyCode() == 8)) {
					// System.out.println("in..");
				} else if (e.getKeyCode() == 10) {
					// sizeValue.setText(""+sizeChooser.getSelectedIndex()+1);
					repaintView();
				} else {
					sizeValue.setText("");
				}
			}
		});

		font_label = new JLabel("Font ");
		font_label.setFont(new Font("Arial", Font.BOLD, 12));
		style_label = new JLabel("Style              ");
		style_label.setFont(new Font("Arial", Font.BOLD, 12));
		size_label = new JLabel("Size       ");
		size_label.setFont(new Font("Arial", Font.BOLD, 12));
		fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		fontList = new JList(fontNames);
		fontList.setSelectedIndex(0);
		fontList.setAutoscrolls(true);
		fontList.setVisibleRowCount(8);
		fontList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				fontName.setText(fontNames[fontList.getSelectedIndex()]);
				repaintView();
			}
		});
		makeDefaultSizes();
		sizeChooser = new JList(defaultSizes);
		sizeChooser.setSelectedIndex(9);
		sizeChooser.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				sizeValue.setText("" + (sizeChooser.getSelectedIndex() + 1));
				repaintView();
			}
		});
		sizeScroller = new JScrollPane(sizeChooser);

		ok = new JButton("OK");
		cancel = new JButton("Cancel");

		fontScroller = new JScrollPane(fontList);
		fontStyle = new JList(styles);
		fontStyle.setSelectedIndex(0);

		Container c = this.getContentPane();
		c.setLayout(new GridLayout());
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(1, 1, 1, 1);
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridheight = 1;
		gc.gridwidth = 10;
		gc.gridx = 0;
		gc.gridy = 0;

		panel.add(font_label, gc);
		gc.gridy = 1;
		panel.add(fontName, gc);

		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridheight = 7;
		gc.gridx = 0;
		gc.gridy = 2;
		panel.add(fontScroller, gc);

		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridwidth = 5;
		gc.gridheight = 1;
		gc.gridx = 10;
		gc.gridy = 0;
		panel.add(style_label, gc);
		gc.gridy = 1;
		panel.add(styleName, gc);

		gc.gridheight = 4;
		gc.gridy = 2;
		fontStyle.setBorder(BorderFactory.createLineBorder(Color.blue));
		fontStyle.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				styleName.setText(styles[fontStyle.getSelectedIndex()]);
				repaintView();
			}
		});
		panel.add(fontStyle, gc);

		gc.gridwidth = 5;
		gc.gridheight = 1;
		gc.gridx = 15;
		gc.gridy = 0;
		panel.add(size_label, gc);
		gc.gridy = 1;
		panel.add(sizeValue, gc);

		gc.gridy = 2;
		gc.gridheight = 4;
		// sizeChooser.setSelectedIndex(9);
		panel.add(sizeScroller, gc);

		samplePanel = new JPanel();
		samplePanel.setLayout(new GridLayout());
		sampleView = new JLabel(sample);
		sampleView.setBorder(BorderFactory.createLoweredBevelBorder());
		samplePanel.setBorder(BorderFactory.createTitledBorder("Sample"));
		samplePanel.setLayout(new BorderLayout());
		samplePanel.add(sampleView, BorderLayout.CENTER);
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridheight = 5;
		gc.gridwidth = GridBagConstraints.REMAINDER;
		gc.gridx = 0;
		gc.gridy = 10;
		panel.add(samplePanel, gc);

		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.gridx = 21;
		gc.gridy = 2;
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				font = new FontStyle();
				font.setColor(Color.black);
				font.setFontName(fontNames[fontList.getSelectedIndex()]);
				font.setSize(sizeChooser.getSelectedIndex() + 1);
				font.setStyle(fontStyle.getSelectedIndex());
				dispose();
			}
		});
		panel.add(ok, gc);

		gc.gridy = 4;
		cancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				font = null;
				dispose();
			}
		});
		panel.add(cancel, gc);

		c.add(panel);

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addKeyListener(this);
		sizeChooser.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyChar());
			}
		});

		int wid = 400;
		int het = 400;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - wid) / 2;
		int y = (dim.height - het) / 2;
		setBounds(x, y, wid, het);
		this.setSize(wid, het);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	private void escapePressed() {
		this.dispose();
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode() + ":" + e.getKeyChar());
		if (e.getKeyCode() == 27)
			escapePressed();

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void makeDefaultSizes() {
		defaultSizes = new Integer[100];
		int si = 1;
		for (int i = 0; i < defaultSizes.length; i++) {
			defaultSizes[i] = si;
			si++;
		}
	}

	private void repaintView() {
		sampleView.setFont(new Font(fontNames[fontList.getSelectedIndex()], fontStyle.getSelectedIndex(), sizeChooser
				.getSelectedIndex() + 1));
	}

	private int searchIndex(String search, String[] data) {
		int index = -1;
		for (int i = 0; i < data.length; i++) {
			if (data[i].toUpperCase().startsWith(search.toUpperCase())) {
				index = i;
				break;
			}
		}
		return index;
	}
}
