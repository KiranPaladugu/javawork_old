package com.view;

import javax.swing.JPanel;

public class Rook extends JPanel implements Piece{
	private static final long serialVersionUID = -3640671631063360631L;

	public int getType() {		
		return 0;
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
