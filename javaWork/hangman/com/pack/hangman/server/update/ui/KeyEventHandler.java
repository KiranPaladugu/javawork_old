package com.pack.hangman.server.update.ui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

public class KeyEventHandler implements KeyListener {

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		// System.out.println("KeyPressed:"+e.getKeyChar());
		// System.out.println("Key CODE:"+e.getKeyCode());
		if (e.getKeyCode() == 10) {
			// System.out.println("You Pressed ENTER.");
			Component cmp = e.getComponent();
			if (cmp.equals(WindowPanel.newWord)) {
				String str = WindowPanel.newWord.getText();
				if ((str != null) && (!str.equals(""))) {
					str = str.trim();
					if (!ServerUpdateUtils.isDuplicate(PreviousDataPanel.list, str)) {
						PreviousDataPanel.list.add(str);
						WindowPanel.saved = false;
					} else {
						JOptionPane.showMessageDialog(null, "Duplicate data found /not added...", "info",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				WindowPanel.newWord.setText("");
				WindowPanel.newWord.requestFocusInWindow();
			}
		}
		if (e.getComponent().equals(PreviousDataPanel.list)) {
			if (e.getKeyCode() == 127) {
				int index = PreviousDataPanel.list.getSelectedIndex();
				if (index != -1) {
					// System.out.println("Removing :" +
					// PreviousDataPanel.list.getItem(index) + " at:" + index);
					PreviousDataPanel.list.remove(index);
					WindowPanel.saved = false;
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
