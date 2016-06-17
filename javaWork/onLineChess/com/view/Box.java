package com.view;

import java.awt.Color;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.resources.PropertyFinder;

public class Box extends JPanel implements DropTargetListener {
	private String name="";
	private JLabel nameLbl;	
	private PieceImage pieceImg;
	private Piece piece;
	private static final long serialVersionUID = 3853375637568712243L;
	public Box() {
		init();		
	}
	private void init(){
		setBorder(BorderFactory.createLineBorder(Color.gray,1));
		setLayout(null);
		pieceImg = new PieceImage();
		pieceImg.setBounds(getX(), getY(), 72, 64);		
		add(pieceImg);
		setComponentZOrder(pieceImg, 0);
		//addMouseMotionListener(this);
		
	}
	public void setName(String name){
		super.setName(name);
		this.name = name;		
	}
	public void setPiece(Piece piece){
		this.piece = piece;
	}
	public Piece getPiece(){
		return piece;
	}
	public void initPieceImage(String name){
		if (name.contains("1") || name.contains("2") || name.contains("7") || name.contains("8")) {
			try {
				pieceImg.setPiceImage(PropertyFinder.getProperty("board.piece." + name));
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
	}
	public String getName(){
		super.getName();
		return name;
	}
	public void dispalyName(boolean flag){
		if(flag){
			nameLbl=new JLabel(name);
			nameLbl.setBounds(25, 30, 35, 20);
			setComponentZOrder(nameLbl, 0);
			add(nameLbl);
		}else{
			//remove(nameLbl);
		}
	}
	public void setNameColor(Color color){
		nameLbl.setForeground(color);
	}
	public void setNameBorder(Border border){
		nameLbl.setBorder(border);
	}
	public void mouseClicked(MouseEvent e) {
		setBorder(BorderFactory.createLineBorder(Color.green,2));
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
	public void mousePressed(MouseEvent e) {
		
	}
	public void mouseReleased(MouseEvent e) {
		
	}
	public void dragEnter(DropTargetDragEvent dtde) {
		
	}
	public void dragExit(DropTargetEvent dte) {
		
	}
	public void dragOver(DropTargetDragEvent dtde) {
		
	}
	public void drop(DropTargetDropEvent dtde) {
		System.out.println("Dropped...");
	}
	public void dropActionChanged(DropTargetDragEvent dtde) {
		
	}	
}
