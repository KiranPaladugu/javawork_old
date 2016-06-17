package com.view;

public interface Piece {
	public static final float PAWN=1.0f;
	public static final float KNIGHT = 3.0f;
	public static final float BISHOP=3.5f;
	public static final float ROOK=5.0f;
	public static final float QUEEEN=9.0f;
	public static final float KING=2.0f;
	public static final int PAWN_TYPE=1;
	public static final int KNIGHT_TYPE=2;
	public static final int BISHOP_TYPE=3;
	public static final int ROOK_TYPE=4;
	public static final int QUEEN_TYPE=5;
	public static final int KING_TYPE=6;
	public int getType();
	public int getColor();
	public boolean isWhite();
	public boolean isBlack();
	public int getMove();
	public boolean isAlive();
	public void setAlive(boolean flag);
	public PieceMove getPieceMove();
}
