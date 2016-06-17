package com.pack.hangman.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pack.hangman.utils.InputUtils;
import com.pack.hangman.utils.WordFetcher;

public class ViewPanel extends JPanel {
	public static JLabel view;
	public String vv;

	public ViewPanel() {

		init();
	}

	public String getView() {
		return vv;
	}

	private void init() {
		view = new JLabel();
		vv = WordFetcher.getNewWord();
		view = new JLabel(InputUtils.createView(vv));
		view.setFont(new Font("Arial", Font.BOLD, 20));
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.add(view);
	}

}
