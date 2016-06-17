package com.pack.hangman.server.update.ui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MouseEventHandler implements MouseMotionListener, MouseListener {

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Component component = e.getComponent();
		if (component.equals(WindowPanel.ver)) {
			JTextField jf = (JTextField) component;
			// jf.setEditable(true);
			// jf.setEnabled(true);
			jf.requestFocusInWindow();
		}
		if (component.equals(PreviousDataPanel.list)) {
			if (SwingUtilities.isRightMouseButton(e)) {
				System.out.println("Mouse right clicked on LIST");
				if (PreviousDataPanel.list.isFocusOwner()) {
					System.out.println("Mouse right clicked on LIST with focus..");
				}
			}
		}

	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseMoved(MouseEvent e) {

		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
