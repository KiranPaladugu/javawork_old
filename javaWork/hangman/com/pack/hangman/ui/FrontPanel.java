package com.pack.hangman.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FrontPanel extends JPanel implements KeyListener {
	private GridBagConstraints gc;
	private ViewPanel view;
	private InputPanel input;
	private PreviousDataPanel list;
	private ImagePanel image;
	private HangmanFrame frame;

	public FrontPanel(HangmanFrame frame) {
		this.frame = frame;
		init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.insets = new Insets(3, 3, 3, 3);
		gc.gridheight = 2;
		gc.gridwidth = GridBagConstraints.REMAINDER;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.ipady = 20;
		view = new ViewPanel();
		this.add(view, gc);

		gc.gridwidth = 10;
		gc.gridy = 2;
		gc.gridx = 0;
		gc.ipady = 15;
		gc.gridheight = 2;
		input = new InputPanel(view.getView());
		this.add(input, gc);
		// gc.weighty=0.1;

		// gc.fill=GridBagConstraints.BOTH;
		gc.gridwidth = GridBagConstraints.REMAINDER;
		gc.gridx = 10;
		gc.gridy = 2;
		gc.ipady = 20;
		gc.gridheight = GridBagConstraints.REMAINDER;
		// gc.weighty=1;
		image = new ImagePanel();
		;
		this.add(image, gc);

		gc.gridwidth = GridBagConstraints.RELATIVE;
		gc.gridx = 0;
		gc.gridy = 4;
		gc.gridheight = GridBagConstraints.REMAINDER;
		// gc.gridy=4;
		list = new PreviousDataPanel();
		this.add(list, gc);

		this.setVisible(true);
		this.repaint();
		this.setSize(300, 400);
		InputPanel.input.requestFocusInWindow();
	}

	public void keyPressed(KeyEvent e) {
		JOptionPane.showMessageDialog(null, "Please don't Do it on me !!", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
