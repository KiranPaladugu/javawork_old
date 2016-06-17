package com.pack.hangman.server.update.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.pack.hangman.server.data.DataReader;

public class WindowPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel version, word, maxLabel;
	public static JTextField ver;
	public static JTextField newWord;
	private GridBagLayout grid;
	private PreviousDataPanel previous;
	private static JButton save, cancel, exit;
	public static boolean saved = true;

	private GridBagConstraints gc;

	private JTable table;

	public WindowPanel() {
		this.init();
		Border border = BorderFactory.createLineBorder(Color.GRAY);
		this.setBorder(border);
		this.setVisible(true);
		this.setSize(450, 550);
		this.repaint();
		System.out.println("setting size");
		this.newWord.requestFocusInWindow();
	}

	public JTextField getNewWord() {
		return newWord;
	}
	private void init() {
		grid = new GridBagLayout();
		this.setLayout(grid);

		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(1, 1, 1, 1);
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.ipadx = 2;
		gc.ipady = 2;

		version = new JLabel("Version:");
		this.add(version, gc);

		ver = new JTextField();
		String v = new DataReader().getVersion();
		if (v != null) {
			ver.setText(v);
		}
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridwidth = 3;
		gc.gridx = 1;
		ver.setEnabled(false);
		ver.addMouseListener(new MouseEventHandler());
		ver.addFocusListener(new FoucusEventHandler());
		// ver.addActionListener(this);
		this.add(ver, gc);

		word = new JLabel("New Word :");
		gc.fill = GridBagConstraints.NONE;
		gc.gridwidth = 1;
		gc.gridx = 0;
		gc.gridy = 1;
		this.add(word, gc);

		newWord = new JTextField(15);
		gc.gridx = 1;
		gc.gridwidth = 3;
		gc.fill = GridBagConstraints.HORIZONTAL;
		newWord.addKeyListener(new KeyEventHandler());
		this.add(newWord, gc);

		maxLabel = new JLabel("* Word should contain maximum of 15 chars.");
		maxLabel.setForeground(Color.red);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 1;
		gc.gridy = 2;
		this.add(maxLabel, gc);

		save = new JButton("Save");
		gc.gridwidth = 1;
		gc.gridx = 1;
		gc.gridy = 3;
		save.addActionListener(new ButtonActionEventHandler());
		this.add(save, gc);

		cancel = new JButton("Cancel");
		gc.gridx = 2;
		gc.gridy = 3;
		cancel.addActionListener(new ButtonActionEventHandler());
		this.add(cancel, gc);

		exit = new JButton("Exit");
		gc.gridx = 3;
		gc.gridy = 3;
		exit.addActionListener(new ButtonActionEventHandler());
		this.add(exit, gc);

		previous = new PreviousDataPanel();
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 1;
		gc.gridy = 4;
		gc.gridwidth = 4;
		gc.gridheight = 30;
		this.add(previous, gc);
	}

	public void setNewWord(String newWord) {
		this.newWord.setText(newWord);
	}

	public void setVersion(String txt) {
		ver.setText(txt);
	}

}
