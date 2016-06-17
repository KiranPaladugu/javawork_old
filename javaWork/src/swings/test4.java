package swings;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class test4 {
	static JTextField tf;
	static JButton b;

	public static void main(String[] args) {

		class DataDialog extends JDialog {
			public String result;

			public DataDialog(Frame parent, boolean modal, String[] data) {
				super(parent, modal);

				final JComboBox bx = new JComboBox(data);
				add(bx, BorderLayout.WEST);
				final JButton b = new JButton("OKAY");
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						result = (String) bx.getSelectedItem();
						setVisible(false);
						dispose();
					}
				});
				add(b, BorderLayout.EAST);
				pack();
				setLocationRelativeTo(parent);
			}
		}

		Runnable r = new Runnable() {
			public void run() {
				final JFrame f = new JFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// text field to hold result
				tf = new JTextField();
				f.add(tf, BorderLayout.NORTH);

				b = new JButton("Get Data From DB");
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						// disable button so you can't try to retrieve while
						// the previous request is going on
						b.setEnabled(false);
						// it could take a while so retrieve the data in
						// another thread
						Runnable r = new Runnable() {
							public void run() {
								// simulate retrieving data from db
								try {
									Thread.sleep(1000);
								} catch (InterruptedException ie) {
								}
								// this is simulated data
								String[] s = { "one", "two", "three", "four" };
								// create dialog
								DataDialog d = new DataDialog(f, true, s);
								d.setVisible(true);
								// get data from public variable in DataDialog
								// and show data in text field in frame
								tf.setText(d.result);
								// re-enable button
								b.setEnabled(true);
							}
						};
						new Thread(r).start();
					}
				});
				f.add(b, BorderLayout.SOUTH);
				f.pack();
				f.setLocationRelativeTo(null);
				f.setVisible(true);
			}
		};
		EventQueue.invokeLater(r);
	}
}
