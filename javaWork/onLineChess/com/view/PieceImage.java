package com.view;

import java.awt.Toolkit;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.common.log.Logger;
import com.resources.PropertyFinder;
import com.resources.ResourceFinder;

public class PieceImage extends JLabel implements DragSourceListener,MouseMotionListener{
	
	public static final String PAWN_BLACK="piece.PAWN_BLACK";
	public static final String KING_BLACK="piece.KING_BLACK";
	public static final String KNIGHT_BLACK="piece.KNIGHT_BLACK";
	public static final String ROOK_BlACK="piece.ROOK_BlACK";
	public static final String QUEEN_BLACK="piece.QUEEN_BLACK";
	public static final String BISHOP_BLACK="piece.BISHOP_BLACK";
	public static final String PAWN_WHITE="piece.PAWN_WHITE";
	public static final String KING_WHITE="piece.KING_WHITE";
	public static final String KNIGHT_WHITE="piece.KNIGHT_WHITE";
	public static final String ROOK_WHITE="piece.ROOK_WHITE";
	public static final String QUEEN_WHITE="piece.QUEEN_WHITE";
	public static final String BISHOP_WHITE="piece.BISHOP_WHITE";
	private String name="";
	
	private static final long serialVersionUID = -7800439919823618232L;
	public PieceImage(){
		//addMouseMotionListener(this);		
	}
	public void setPiceImage(String name){
		try {
			this.name = name;
			setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ResourceFinder.getResource(PropertyFinder.getProperty(name)))));
		} catch (IOException e) {			
			Logger.log("Unable to get Resource...");
		}
	}
	public void dragDropEnd(DragSourceDropEvent dsde) {
		
	}
	public String getPieceName(){
		return name;
	}
	public void dragEnter(DragSourceDragEvent dsde) {
		dsde.getSource();
		System.out.println("got :"+dsde.getSource().getClass());
	}
	public void dragExit(DragSourceEvent dse) {
		
	}
	public void dragOver(DragSourceDragEvent dsde) {
		
	}
	public void dropActionChanged(DragSourceDragEvent dsde) {
		
	}
	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse Dragging .. on :"+e.getSource().getClass());
	}
	public void mouseMoved(MouseEvent e) {		
	}
}
