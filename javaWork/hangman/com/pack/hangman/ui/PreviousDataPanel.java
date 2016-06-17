package com.pack.hangman.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PreviousDataPanel extends JPanel implements ListDataListener {
	public static List list;

	public PreviousDataPanel() {

		init();
	}

	public void contentsChanged(ListDataEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Content Changed ..");

	}

	private void init() {
		list = new List();
		this.setLayout(new GridLayout(1, 1));
		this.setBorder(BorderFactory.createTitledBorder("Previous Data"));
		list.setEnabled(true);
		list.setFont(new Font("Arial", Font.BOLD, 15));
		list.setForeground(Color.RED);
		this.add(list);
	}

	public void intervalAdded(ListDataEvent e) {
		// TODO Auto-generated method stub

	}

	public void intervalRemoved(ListDataEvent e) {
		// TODO Auto-generated method stub

	}

	public void setData(String newChar) {
		list.add(newChar);
	}
}
