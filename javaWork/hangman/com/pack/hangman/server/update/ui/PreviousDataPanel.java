package com.pack.hangman.server.update.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pack.hangman.server.data.DataReader;

public class PreviousDataPanel extends JPanel implements MouseListener {
	private JLabel previousData;
	public static List list;
	private GridBagConstraints gc;
	private GridBagLayout g;
	private ListMenu listMenu;

	public PreviousDataPanel() {
		init();
	}

	public void init() {
		g = new GridBagLayout();
		gc = new GridBagConstraints();
		this.setLayout(g);
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.setBorder(BorderFactory.createRaisedBevelBorder());

		gc.fill = GridBagConstraints.NONE;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.ipady = 10;
		gc.gridx = 0;
		gc.gridy = 0;

		previousData = new JLabel("Previous Data ");
		previousData.setForeground(Color.DARK_GRAY);
		this.add(previousData, gc);

		gc.fill = GridBagConstraints.BOTH;
		gc.gridheight = 20;
		gc.ipady = 100;
		gc.gridy = 1;
		listMenu = new ListMenu();
		DataReader reader = new DataReader();
		String data[] = reader.getData();
		list = new List();
		if (data != null) {
			for (int i = 0; i < data.length; i++)
				list.add(data[i]);
		}
		list.addKeyListener(new KeyEventHandler());
		list.addMouseListener(this);
		list.add(listMenu);
		this.add(list, gc);
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			listMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);

	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);

	}
}
