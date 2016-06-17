package com.tcs.berReader.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class StatusPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3253800370702443171L;
	private JProgressBar progressBar;
	private JLabel lable;
	private GridBagConstraints gc;
	public StatusPane() {
		
		gc = new GridBagConstraints();
		gc.insets = new Insets(1, 1, 1, 1);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.BOTH;
		gc.gridheight = GridBagConstraints.REMAINDER;
		
		setLayout(new GridBagLayout());
		init();
		setVisible(false);
	}
	private void init() {
		lable = new JLabel("status..");
		
		gc.gridx =0;
		gc.gridy =0;
		gc.gridheight=1;
		gc.gridwidth = 15;
		gc.weightx =1;
		gc.weighty =1 ;
		
		add(lable , gc);
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(5);
		progressBar.setValue(1);
		progressBar.setSize(10, 10);
		
		gc.gridx =16;
		gc.gridy =0;
		gc.gridheight=1;
		gc.gridwidth = 5;
		gc.weightx =1;
		gc.weighty =1 ;
		
		add(progressBar);
	}
}
