package com.pack.hangman.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -7711810824547141508L;
	public static String images[];
	private String inImage;
	private String ext = ".png";
	public static JLabel lable_image;

	public ImagePanel() {
		init();
	}

	private void init() {
		images = new String[7];
		for (int i = 0; i < 7; i++) {
			images[i] = new String("H/" + i + ext);
		}
		this.setLayout(new GridLayout(1, 1));
		inImage = "H/0" + ext;
		Toolkit toolKit = Toolkit.getDefaultToolkit();
		Image image = toolKit.getImage(inImage);
		ImageIcon imgIcon = new ImageIcon(inImage);
		lable_image = new JLabel();
		lable_image.setIcon(imgIcon);
		// lable_image.setVisible(false);
		this.add(lable_image);
		// this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
	}

}
