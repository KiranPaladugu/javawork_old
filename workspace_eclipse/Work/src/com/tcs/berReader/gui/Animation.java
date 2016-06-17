package com.tcs.berReader.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tcs.ber.resource.PropertyFinder;
import com.tcs.ber.resource.ResourceFinder;

public class Animation extends Thread {

	private JFrame frame;
	private volatile transient boolean stop;
	private volatile transient boolean flag;
	private JPanel glassPanel;

	public Animation(JFrame frame) {
		this.frame = frame;
		if (this.frame != null)
			stop = false;
		else
			stop = true;
		flag = false;
		if(isAnimationSupported()){
			this.start();
		}else{
			stop = true;
		}
	}

	private boolean isAnimationSupported() {
		return true;
	}

	@Override
	public void run() {
		while (!stop) {
			JPanel glassPanel = getGlasspane();

			if (ApplicationContext.isLoading() && !flag) {
				glassPanel.setVisible(true);
				flag = true;
			} else {
				if (flag && !ApplicationContext.isLoading()) {
					glassPanel.setVisible(false);
					flag = false;
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return
	 */
	private JPanel getGlasspane() {
		if (glassPanel == null) {
			Component glassPane = frame.getGlassPane();
			glassPanel = (JPanel) glassPane;
			glassPanel.setLayout(new BorderLayout());
			JLabel lbl = new JLabel("\t\tPlease wait while loading.....");
			try {
				lbl.setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.Loading")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			lbl.setFont(new Font("Arial", Font.BOLD, 25));
			lbl.setForeground(Color.GREEN);
			lbl.setBackground(Color.LIGHT_GRAY);
			glassPanel.add(lbl, BorderLayout.CENTER);
			JLabel lbl_west = new JLabel("                               ");
			glassPanel.add(lbl_west, BorderLayout.WEST);
		}
		return glassPanel;
	}
}
