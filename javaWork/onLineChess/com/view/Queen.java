package com.view;

import javax.swing.JPanel;

public class Queen extends JPanel implements Piece {
	private static final long serialVersionUID = 1725824358771952800L;

	public int getColor() {
		return 0;
	}

	public int getMove() {
		return 0;
	}

	public PieceMove getPieceMove() {
		return null;
	}

	public int getType() {
		return 0;
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
