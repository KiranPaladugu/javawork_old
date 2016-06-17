package com.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ChessBoard extends JPanel implements MouseListener,MouseMotionListener{
	private static final long serialVersionUID = -6436242363054780289L;
	public final int A=1;
	public final int B=2;
	public final int C=3;
	public final int D=4;
	public final int E=5;
	public final int F=6;
	public final int G=7;
	public final int H=8;
	private int  select= 0;
	private boolean s=false;
	private Box selectedBox;
	public Box box[][];
	public char ch[]={'a','b','c','d','e','f','g','h'};
	public ChessBoard() {
		init();
	}
	private void init(){
		box=new Box[8][8];
		setLayout(new GridLayout(8,8));
		int rowcount=1;
		int colcount=1;
		for(int i=7;i>-1;i--){
			for(int j=0;j<8;j++){			
				box[i][j]=new Box();
				box[i][j].getAccessibleContext();
				if(((rowcount==1)&&(colcount==1))||((rowcount==2)&&(colcount==2))){
					box[i][j].setBackground(new Color(255,255,204));
				} else{
					box[i][j].setBackground(new Color(153,50,0));
				}
//				box[i][j].setName("["+ch[j]+"] ["+(i+1)+"]");
//				box[i][j].dispalyName(true);
//				box[i][j].setNameColor(Color.red);
//				box[i][j].setNameBorder(BorderFactory.createLineBorder(Color.green));
				box[i][j].initPieceImage((i+1)+"."+ch[j]);
				add(box[i][j]);
				colcount++;
				if(colcount>2){
					colcount=1;
				}
			}
			rowcount++;
			if(rowcount>2){
				rowcount=1;
			}
		}
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public Component getBox(int row,int col){
		return box[row][col];
	}
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		Box comp = (Box)getComponentAt(point);
		select++;
		if(s){			
			selectedBox.setBorder(BorderFactory.createLineBorder(Color.gray, 1));			
			s=false;
			if(!selectedBox.equals(comp)){
				comp.setBorder(BorderFactory.createLineBorder(Color.green, 2));
				selectedBox = comp;
				s=true;
			}
		} else {
			selectedBox = comp;
			comp.setBorder(BorderFactory.createLineBorder(Color.green, 2));	
			s=true;
		}		
	}
	public Box[][] getBoxes(){
		return box;
	}
	public void mouseEntered(MouseEvent e) {		
	}
	public void mouseExited(MouseEvent e) {		
	}
	public void mousePressed(MouseEvent e) {		
	}
	public void mouseReleased(MouseEvent e) {		
	}
	public void mouseDragged(MouseEvent e) {		
	}
	public void mouseMoved(MouseEvent e) {
		
	}
}
