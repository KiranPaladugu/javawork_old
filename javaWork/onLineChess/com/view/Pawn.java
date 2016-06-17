package com.view;

import javax.swing.JPanel;

public class Pawn extends JPanel implements Piece  {
	private static final long serialVersionUID = -8347680899243488892L;
	private int type=1;
	//private PieceImage image;
	public int getType() {		
		return type;
	}
	public int getColor() {
		return 0;
	}
	public int getMove() {
		return 0;
	}
	public PieceMove getPieceMove() {
		return null;
	}
	public boolean isAlive() {
		return false;
	}
	public boolean isBlack() {
		return false;
	}
	public boolean isWhite() {
		return false;
	}
	public void setAlive(boolean flag) {
		
	}
	

}
