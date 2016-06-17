package com.pack.hangman.server.update.ui;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListMenu extends PopupMenu implements ActionListener {
	MenuItem delete;

	public ListMenu() {
		init();
	}

	public void actionPerformed(ActionEvent e) {
		e.getSource();
		System.out.println(e.getSource());
	}

	public void init() {
		delete = new MenuItem("delete");
		delete.addActionListener(this);
		this.setEnabled(true);

	}
}
