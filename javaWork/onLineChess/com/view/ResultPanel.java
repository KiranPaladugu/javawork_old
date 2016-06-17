package com.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ResultPanel extends JPanel  implements MouseListener   {
	private static final long serialVersionUID = -5543637738915908278L;
	public ResultPanel() {
		init();
	}
	public void init(){
		setBorder(BorderFactory.createLineBorder(Color.gray));
		setToolTipText("width:"+this.getWidth()+" Heigth:"+this.getHeight());
	}
	public void mouseClicked(MouseEvent e) {		
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
	public void mousePressed(MouseEvent e) {
		
	}
	public void mouseReleased(MouseEvent e) {
		
	}
}
