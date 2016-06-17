package com.pack.hangman.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pack.hangman.utils.InputUtils;
import com.pack.hangman.utils.WordFetcher;

public class InputPanel extends JPanel implements KeyListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JTextField input;
	private JButton reStart;
	private String WORD;
	private static String INPUT = "";
	private static int WON = -1;

	public InputPanel(String Word) {
		this.WORD = Word;
		init();
	}

	public void actionPerformed(ActionEvent e) {
		int choice = -1;
		if (WON == (-1)) {
			choice = JOptionPane.showConfirmDialog(null, "Are You SURE ??", "Confirmation", JOptionPane.YES_NO_OPTION);
		} else {
			choice = 0;
		}
		if (choice == 0) {
			reset();
		}
		// System.exit(0);
	}

	private void init() {
		input = new JTextField();
		reStart = new JButton("ReStart");
		// this.setLayout(new GridBagLayout());
		this.setLayout(new GridLayout(1, 2));
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(3, 3, 3, 3);
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		reStart.addActionListener(this);
		reStart.addKeyListener(this);
		this.add(reStart, gc);
		gc.fill = GridBagConstraints.HORIZONTAL;
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		gc.gridheight = 2;
		gc.gridx = 1;
		input.setFont(new Font("Arial", Font.BOLD, 30));
		input.addKeyListener(this);
		input.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		input.requestFocusInWindow();
		this.add(input, gc);
		// this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setSize(200, 200);
		this.setVisible(true);
	}

	public void keyPressed(KeyEvent e) {
		// Logger.log("clearing....");
		input.setText("");
		if ((e.getKeyCode() >= 65) && (e.getKeyCode() <= 90)) {
			// Logger.log("Checking.....");
			char ch = e.getKeyChar();
			// Logger.log("Adding to list..");
			if (!InputUtils.isDuplicate(PreviousDataPanel.list, "" + ch)) {
				PreviousDataPanel.list.add(("" + ch).toUpperCase());

			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() >= 65) && (e.getKeyCode() <= 90)) {
			char ch = e.getKeyChar();
			INPUT = ViewPanel.view.getText().trim();
			String ss = InputUtils.evaluateInputAndView(INPUT, WORD, ch);
			ViewPanel.view.setText(ss);
			input.setText(("" + ch).toUpperCase());
			ImagePanel.lable_image.setIcon(new ImageIcon(ImagePanel.images[InputUtils.mistakes]));
			if (InputUtils.mistakes >= 6) {
				WON = 0;
				input.setEditable(false);
				input.setEnabled(false);
				ResultSplash rs = new ResultSplash(ResultSplash.LOST);
				rs.showWindow();
				ViewPanel.view.setText(InputUtils.setCompletedView(WORD));
				JOptionPane.showMessageDialog(null, "YOU FAILED..", "LOST!", JOptionPane.INFORMATION_MESSAGE);
				int x = JOptionPane.showConfirmDialog(null, "YOU LOST!!\n Do you want to play again ?", "Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (x == 0) {
					reset();
				} else if (x == 1) {

				} else {
					System.exit(0);
				}

			}
			if (InputUtils.isCompleted(ss)) {
				WON = 1;
				input.setEditable(false);
				input.setEnabled(false);
				ResultSplash rs = new ResultSplash(ResultSplash.WON);
				rs.showWindow();
				JOptionPane.showMessageDialog(null, "YOU WON !!", "WON!", JOptionPane.INFORMATION_MESSAGE);
				int x = JOptionPane.showConfirmDialog(null, "YOU WON!!\n Do you want to play again ?", "Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (x == 0) {
					reset();
				} else if (x == 1) {

				} else {
					System.exit(0);
				}

			}
		}
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void reset() {
		InputUtils.mistakes = 0;
		WON = -1;
		InputUtils.msg = "";
		InputUtils.w = "";
		String str = WordFetcher.getNewWord();
		String msg = InputUtils.createView(str);
		ViewPanel.view.setText(msg);
		this.WORD = str;
		PreviousDataPanel.list.removeAll();
		input.setText("");
		input.setEditable(true);
		input.setEnabled(true);
		input.requestFocusInWindow();
		ImagePanel.lable_image.setIcon(new ImageIcon(ImagePanel.images[InputUtils.mistakes]));
	}
}
