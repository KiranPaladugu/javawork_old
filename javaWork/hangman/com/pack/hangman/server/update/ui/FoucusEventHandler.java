package com.pack.hangman.server.update.ui;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class FoucusEventHandler implements FocusListener {

	public void focusGained(FocusEvent e) {
		Component component = e.getComponent();
		if (component.equals(WindowPanel.ver)) {
			JTextField jf = (JTextField) component;
			// jf.setEditable(true);
			jf.setEnabled(true);
		}
	}

	public void focusLost(FocusEvent e) {
		Component component = e.getComponent();
		if (component.equals(WindowPanel.ver)) {
			JTextField jf = (JTextField) component;
			// jf.setEditable(false);
			jf.setEnabled(false);
		}

	}

}
