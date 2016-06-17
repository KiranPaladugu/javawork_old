package com.tcs.berReader.gui;

import java.awt.Component;
import java.awt.Font;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;

import com.tcs.ber.resource.PropertyFinder;
import com.tcs.ber.resource.ResourceFinder;

public class ListRenderer extends DefaultListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8836481019337073459L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component comp= super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		try {
			setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.BER")));
		} catch (IOException e) {
		}		
		Font font = comp.getFont();		
		comp.setFont(new Font(font.getFontName(), Font.PLAIN, 12));
		JLabel lbl = (JLabel)comp;
		lbl.setBorder(new EmptyBorder(4, 1, 4, 1));
		return lbl;
	}
}
