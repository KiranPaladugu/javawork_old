package com.pack.hangman.server.update.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.pack.hangman.server.data.DataSaver;

public class ButtonActionEventHandler implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		JButton cmp = (JButton) e.getSource();
		System.out.println("Button action performed.");
		System.out.println(e.getActionCommand());
		if (e.getActionCommand().equals("Exit")) {
			if (!WindowPanel.saved) {
				int option = JOptionPane.showConfirmDialog(null, "Data not saved \n Are you sure ?", "",
						JOptionPane.YES_NO_OPTION);
				if (option == 0) {
					System.exit(0);
				} else if (option == 1) {
					return;
				}
			}
			System.exit(0);
		}
		if (e.getActionCommand().equals("Save")) {
			DataSaver saver = new DataSaver();
			saver.save(PreviousDataPanel.list.getItems());
			WindowPanel.saved = true;
			JOptionPane.showMessageDialog(null, "Data Saved", "Save Compleated", JOptionPane.INFORMATION_MESSAGE);
		}

	}

}
