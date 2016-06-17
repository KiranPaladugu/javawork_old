import java.awt.GraphicsEnvironment;
import java.awt.Label;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reg.Register;

public class FindHandler extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private int startIndex = 0;
	private Label l1, l2;
	private TextField tf, tr;
	private JButton find_btn, find_next, replace, replace_all, cancel;
	private JCheckBox caseSensitive;
	private ViewPanel samp;


	public FindHandler(ViewPanel mynote) {
		super();
		samp = mynote;
		l1 = new Label("Find What: ");
		l2 = new Label("Replace With: ");
		tf = new TextField(30);
		tf.addKeyListener(new KeyListernerHandle());
		tr = new TextField(30);
		tr.addKeyListener(new KeyListernerHandle());
		find_btn = new JButton("Find");
		find_btn.addKeyListener(new KeyListernerHandle());
		find_next = new JButton("Find Next");
		find_next.addKeyListener(new KeyListernerHandle());
		replace = new JButton("Replace");
		replace.addKeyListener(new KeyListernerHandle());
		replace_all = new JButton("Replace All");
		replace_all.addKeyListener(new KeyListernerHandle());
		cancel = new JButton("Cancel");
		cancel.addKeyListener(new KeyListernerHandle());
		caseSensitive = new JCheckBox("CaseSensitive");
		caseSensitive.addKeyListener(new KeyListernerHandle());
		setLayout(null);
		int label_w = 80;
		int label_h = 25;
		int tf_w = 120;
		l1.setBounds(10, 10, label_w, label_h);
		add(l1);
		tf.setBounds(10 + label_w, 10, tf_w, 20);
		add(tf);
		l2.setBounds(10, 10 + label_h + 10, label_w, label_h);
		add(l2);
		tr.setBounds(10 + label_w, 10 + label_h + 10, tf_w, 20);
		add(tr);
		caseSensitive.setBounds(10, 70, 150, 25);
		add(caseSensitive);
		find_btn.setBounds(220, 10, 100, 25);
		find_btn.setMnemonic('F');
		add(find_btn);
		find_btn.addActionListener(this);
		find_next.setBounds(220, 40, 100, 25);
		add(find_next);
		find_next.addActionListener(this);
		replace.setBounds(220, 70, 100, 25);
		add(replace);
		replace.addActionListener(this);
		replace_all.setBounds(220, 100, 100, 25);
		add(replace_all);
		replace_all.addActionListener(this);
		cancel.setBounds(220, 130, 100, 25);
		add(cancel);
		cancel.addActionListener(this);
		int w = 350;
		int h = 200;
		setSize(w, h);
		addWindowFocusListener(new WindowFocusListener() {
			
			public void windowLostFocus(WindowEvent e) {				
				Window window = e.getOppositeWindow();
				ViewerFrame frame = (ViewerFrame) Register.getObject(ViewerFrame.class);				
				if(frame != null ){
					Window win = frame;
					if(!win.equals(window)){
						setVisible(false);
					}
				}
			}
			
			public void windowGainedFocus(WindowEvent e) {
				
			}
		});
		addWindowListener(new WindowListener() {
			
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void windowClosing(WindowEvent e) {
				ViewerFrame frame = (ViewerFrame) Register.getObject(ViewerFrame.class);
				if(frame != null ){
					frame.focFindVis = false;
					frame.focFindVis = false;
				}
			}
			
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.setResizable(false);
		// set window position
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		setLocation(center.x - w / 2, center.y - h / 2);
		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				JOptionPane.showMessageDialog(null, "Window State Changed:" + e.getNewState());
			}
		});
		setAlwaysOnTop(true);
		setResizable(false);
		setVisible(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// this.setModal(true);		
	}

	public class KeyListernerHandle implements KeyListener {

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if (e.isControlDown()) {
				System.out.println("Key Pressed:" + e.getKeyCode());
				if ((e.getKeyCode() == KeyEvent.VK_F) || (e.getKeyCode() == KeyEvent.VK_ENTER)) {
					if (!tf.getText().equals(""))
						find(tf.getText());
				} else if (e.getKeyCode() == KeyEvent.VK_N) {
					if (!tf.getText().equals(""))
						find_next();
				} else if (e.getKeyCode() == KeyEvent.VK_R) {
					if (!tf.getText().equals(""))
						replace(tr.getText());
				} else if (e.getKeyCode() == KeyEvent.VK_L) {
					if (!tf.getText().equals(""))
						replace_all();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					JOptionPane.showConfirmDialog(null, "EscapePressed");
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == find_btn) {
			if (!tf.getText().equals(""))
				find(tf.getText());
		} else if (e.getSource() == find_next) {
			if (!tf.getText().equals(""))
				find_next(tf.getText());
		} else if (e.getSource() == replace) {
			if (!tf.getText().equals(""))
				replace(tf.getText());
		} else if (e.getSource() == replace_all) {
			if (!tf.getText().equals(""))
				replace_all();
		} else if (e.getSource() == cancel) {
			ViewerFrame frame = (ViewerFrame) Register.getObject(ViewerFrame.class);
			if(frame != null ){
				frame.focFindVis = false;
				frame.focFindVis = false;
			}
			this.setVisible(false);
		}
	}

	public void find(String str) {
		int select_start = -1;
		if (caseSensitive.isSelected()) {
			select_start = samp.logView.getText().indexOf(str);
		} else {
			select_start = samp.logView.getText().toUpperCase().indexOf(str.toUpperCase());
		}
		if (select_start == -1) {
			startIndex = 0;
			JOptionPane.showMessageDialog(null, "Could not find " + str + "!");
			return;
		} else {
			int ind = 0;
			if (caseSensitive.isSelected()) {
				ind = samp.logView.getText().lastIndexOf(str);
			} else {
				ind = samp.logView.getText().toUpperCase().lastIndexOf(str.toUpperCase());
			}
			if (select_start == ind) {
				startIndex = 0;
			}
		}
		int select_end = select_start + str.length();
		samp.logView.select(select_start, select_end);
		find_next.requestFocusInWindow();
	}

	public void find_next() {

		String selection = samp.logView.getSelectedText();
		try {
			selection.equals("");
		} catch (NullPointerException e) {
			selection = tf.getText();
			try {
				selection.equals("");
			} catch (NullPointerException e2) {
				selection = JOptionPane.showInputDialog("Find:");
				tf.setText(selection);
			}
		}
		try {
			int select_start = samp.logView.getText().indexOf(selection, startIndex);
			int select_end = select_start + selection.length();
			samp.logView.select(select_start, select_end);
			startIndex = select_end + 1;
			if (select_start == samp.logView.getText().lastIndexOf(selection)) {
				startIndex = 0;
			}
		} catch (NullPointerException e) {
		}
		find_next.requestFocusInWindow();
	}

	public void find_next(String str) {
		String selection = samp.logView.getSelectedText();
		try {
			selection.equals("");
		} catch (NullPointerException e) {
			selection = str;
			try {
				selection.equals("");
			} catch (NullPointerException e2) {
				selection = JOptionPane.showInputDialog("Find:");
				tf.setText(selection);
			}
		}
		try {
			int select_start = samp.logView.getText().indexOf(selection, startIndex);
			int select_end = select_start + selection.length();
			samp.logView.select(select_start, select_end);
			startIndex = select_end + 1;
			if (select_start == samp.logView.getText().lastIndexOf(selection)) {
				startIndex = 0;
			}
		} catch (NullPointerException e) {
		}
	}

	public void replace(String str) {
		try {
			find(str);
			samp.logView.replaceSelection(str);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		replace.requestFocusInWindow();
	}

	public void replace_all() {
		if (!tf.getText().equalsIgnoreCase(""))
			samp.logView.setText(samp.logView.getText().replaceAll(tf.getText(), tr.getText()));
		replace_all.requestFocusInWindow();
	}
}