package com.tcs.berReader.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.framework.reg.Register;

public class ResultPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3304011900328306864L;
	private BERListPane listPane;
	private BerViewPane berView;
	private JSplitPane split;
	public ResultPane() {
		Register.register(this);
		this.setLayout(new GridLayout(1,1));
		init();
	}

	private void init() {
		initSplit();
	}

	private void initSplit() {
		listPane = new BERListPane();
		berView = new BerViewPane(null);
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,listPane,berView);
		split.setDividerSize(2);
		add(split);
	}
}
