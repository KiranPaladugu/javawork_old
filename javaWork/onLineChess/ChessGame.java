import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class ChessGame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JToolBar tlbMain = null;

	private JLabel lblCells[] = new JLabel[64];

	private String jPieces[][] = new String[8][8]; // @jve:decl-index=0:

	private JButton btnNewGame = null;

	private JLabel lblStatus = null;

	private int heldX, heldY, heldI = -1;

	// Map the full names of the pieces to their codenames (wRook, wQueen, etc.)
	private Map<String, String> pieceName = new TreeMap<String, String>(); // @jve:decl-index=0:

	private JLabel lblCurrPlayer = null;

	// Stores the current player's move - we can easily match it against
	// the first character of the pieces array
	private char currPlayer = ' ';

	private JButton btnUndo = null;

	private int[][] moves = new int[10][6];

	private String movedPieces[] = new String[10];

	private int currMove = 0;

	/**
	 * This is the default constructor
	 */
	public ChessGame() {
		super();
		initialize();
		buildBoard();
	}

	/**
	 * 70. * This method initializes btnUndo 71. * 72. * @return
	 * javax.swing.JButton 73.
	 */
	private JButton getBtnUndo() {
		if (btnUndo == null) {
			btnUndo = new JButton();
			btnUndo.setText("Undo");
			btnUndo.setEnabled(false);
			btnUndo.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseReleased(java.awt.event.MouseEvent e) {
					undoMove();
				}
			});
		}
		return btnUndo;
	}

	public static void main(String args[]) {
		new ChessGame().setVisible(true);
	}

	/**
	 * 93. * This method initializes this 94. * 95. * @return void 96.
	 */
	private void initialize() {
		this.setSize(671, 555);
		this.setContentPane(getJContentPane());
		this.setTitle("Basic Chess");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * 105. * This method initializes jContentPane 106. * 107. * @return
	 * javax.swing.JPanel 108.
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
			jContentPane.add(getTlbMain(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel . *
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(8);
			gridLayout.setHgap(5);
			gridLayout.setVgap(5);
			gridLayout.setColumns(8);
			jPanel = new JPanel();
			jPanel.setLayout(gridLayout);
			// buildBoard();
		}
		return jPanel;
	}

	private void newGame() {
		resetBoard();
		resetPieces();
	}

	private void resetPieces() {
		jPieces = new String[8][8];
		jPieces[0][0] = "bRook";
		jPieces[0][1] = "bKnight";
		jPieces[0][2] = "bBishop";
		jPieces[0][3] = "bKing";
		jPieces[0][4] = "bQueen";
		jPieces[0][5] = "bBishop";
		jPieces[0][6] = "bKnight";
		jPieces[0][7] = "bRook";
		jPieces[1][0] = "bPawn";
		jPieces[1][1] = "bPawn";
		jPieces[1][2] = "bPawn";
		jPieces[1][3] = "bPawn";
		jPieces[1][4] = "bPawn";
		jPieces[1][5] = "bPawn";
		jPieces[1][6] = "bPawn";
		jPieces[1][7] = "bPawn";

		jPieces[6][0] = "wPawn";
		jPieces[6][1] = "wPawn";
		jPieces[6][2] = "wPawn";
		jPieces[6][3] = "wPawn";
		jPieces[6][4] = "wPawn";
		jPieces[6][5] = "wPawn";
		jPieces[6][6] = "wPawn";
		jPieces[6][7] = "wPawn";
		jPieces[7][0] = "wRook";
		jPieces[7][1] = "wKnight";
		jPieces[7][2] = "wBishop";
		jPieces[7][3] = "wKing";
		jPieces[7][4] = "wQueen";
		jPieces[7][5] = "wBishop";
		jPieces[7][6] = "wKnight";
		jPieces[7][7] = "wRook";
		RepaintPieces();
	}

	private void PaintPiece(String pieceName, int i) {
		try {
			if (pieceName != null && pieceName != "") {
//				InputStream inIcon = ClassLoader.getSystemResourceAsStream("pociu/games/chess/" + pieceName + ".png");
//				BufferedImage imgIcon = ImageIO.read(inIcon);
//				lblCells[i].setIcon(new ImageIcon(imgIcon));
				lblCells[i].setName(pieceName);
				lblCells[i].setForeground(Color.red);
				 System.out.println("Painted " + pieceName + " at " + i);
			} else {
				lblCells[i].setIcon(null);
				// System.out.println("Cleared cell at " + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void RepaintPieces() {
		int i = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (jPieces[x][y] != null && !jPieces[x][y].equals("")) {
					PaintPiece(jPieces[x][y], i);
				} else {
					PaintPiece("", i);
				}
				i++;
			}
		}
	}

	private void ClearHlight(int i, int rowNum) {
		if ((i + rowNum) % 2 == 0) {
			lblCells[i].setBackground(Color.WHITE);
		} else {
			lblCells[i].setBackground(Color.GRAY);
		}
	}

	private void undoMove() {
		if (btnUndo.isEnabled() && currMove > 0) {
			currMove--;
			movePiece(moves[currMove][3], moves[currMove][4], moves[currMove][5], moves[currMove][0], moves[currMove][1],
					moves[currMove][2], true);
		}
	}

	private void resetBoard() {
		currMove = 0;
		pieceName.clear();
		pieceName.put("bRook", "Black Rook");
		pieceName.put("bQueen", "Black Queen");
		pieceName.put("bPawn", "Black Pawn");
		pieceName.put("bKnight", "Black Knight");
		pieceName.put("bBishop", "Black Bishop");
		pieceName.put("bKing", "Black King");
		pieceName.put("wRook", "White Rook");
		pieceName.put("wQueen", "White Queen");
		pieceName.put("wPawn", "White Pawn");
		pieceName.put("wKnight", "White Knight");
		pieceName.put("wBishop", "White Bishop");
		pieceName.put("wKing", "White King");
		pieceName.put("wRook", "White Rook");

		// If we're holding a piece, clear the hover of the cell
		if (heldI >= 0 && heldX >= 0) {
			ClearHlight(heldI, heldX);
		}

		switchPlayer();

		heldX = heldY = heldI = -1;
	}

	private void buildBoard() {
		// First reset the variables, maps, etc.
		resetBoard();

		int rowColor = 0;
		int i = 0;
		for (int x = 0; x <= 7; x++) {
			rowColor++;
			for (int y = 0; y <= 7; y++) {
				lblCells[i] = new JLabel("", JLabel.CENTER);
				lblCells[i].setOpaque(true);
				if (rowColor % 2 == 0) {
					lblCells[i].setBackground(Color.GRAY);
				} else {
					lblCells[i].setBackground(Color.WHITE);
				}

				final int passX = x;
				final int passY = y;
				final int passI = i;

				lblCells[i].addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseEntered(java.awt.event.MouseEvent e) {
						// If we're holding a piece show that along with the
						// cell we're hovering
						if (heldI > 0) {
							lblStatus.setText("Picked up " + pieceName.get(jPieces[heldX][heldY]) + " at "
									+ showBoardRelative(heldX, heldY) + " | Hovering: " + showBoardRelative(passX, passY));
						} else // Just show what we're hovering
						{
							lblStatus.setText("Hovering: " + showBoardRelative(passX, passY));
						}
						// Unless we hover the one we're holding...
						if (passI != heldI) {
							lblCells[passI].setBackground(Color.DARK_GRAY);
						}
					}
				});

				lblCells[i].addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseExited(java.awt.event.MouseEvent e) {
						lblStatus.setText("");
						// Unless we hover the one we're holding...
						if (passI != heldI) {
							// Clear the hover effect
							ClearHlight(passI, passX);
						}
					}
				});

				lblCells[i].addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseReleased(java.awt.event.MouseEvent e) {
						if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
							showCellInfo(passX, passY, passI);
						} else if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
							clickCell(e, passX, passY, passI);
						}
					}
				});

				jPanel.add(lblCells[i]);
				rowColor++;
				i++;
			}
		}
		resetPieces();
	}

	// Translates grid relative coordinates to chess board relative
	// For ex.: 0x0 to 8A
	private String showBoardRelative(int x, int y) {
		String chessCoord = "";
		chessCoord = (x - 8) * -1 + "" + (char) (y + 65);
		return chessCoord;
	}

	private void showCellInfo(int x, int y, int i) {
		if (jPieces[x][y] != null && !jPieces[x][y].equals("")) {
			JOptionPane.showMessageDialog(null, pieceName.get(jPieces[x][y]) + " located at " + showBoardRelative(x, y),
					"Cell Information", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "No piece located at " + showBoardRelative(x, y), "Cell Information",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private boolean isValidMove(int fromX, int fromY) {
		if (jPieces[fromX][fromY].length() > 0 && jPieces[fromX][fromY].charAt(0) == currPlayer) {
			return true;
		} else {
			return false;
		}
	}

	private void movePiece(int fromX, int fromY, int fromI, int toX, int toY, int toI, boolean isUndo) {
		if (fromX == toX && fromY == toY) {
			ClearHlight(fromI, fromX);
			lblStatus.setText("Move canceled.");
		} else if (isValidMove(fromX, fromY) || isUndo == true) {
			PaintPiece(jPieces[fromX][fromY], toI);
			PaintPiece("", fromI);
			ClearHlight(fromI, fromX);
			lblStatus.setText("Moved " + pieceName.get(jPieces[fromX][fromY]) + " from " + showBoardRelative(fromX, fromY)
					+ " to " + showBoardRelative(toX, toY));
			jPieces[toX][toY] = jPieces[fromX][fromY];
			jPieces[fromX][fromY] = "";
			if (currMove > 9) {
				pushbackUndos();
			}
			moves[currMove] = new int[6];
			moves[currMove][0] = fromX;
			moves[currMove][1] = fromY;
			moves[currMove][2] = fromI;
			moves[currMove][3] = toX;
			moves[currMove][4] = toY;
			moves[currMove][5] = toI;
			movedPieces[currMove] = jPieces[fromX][fromY];
			btnUndo.setEnabled(true);
			if (isUndo == false) {
				currMove++;
			}
			switchPlayer();
		} else {
			ClearHlight(fromI, fromX);
			JOptionPane.showMessageDialog(null, "It's the " + lblCurrPlayer.getText(), "Illegal Move",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void pushbackUndos() {
		for (int i = 0; i < 9; i++) {
			moves[i] = moves[i + 1];
			movedPieces[i] = movedPieces[i + 1];
		}
		currMove--;
	}

	private void switchPlayer() {
		// System.out.write(currPlayer);
		if (currPlayer == 'w') {
			currPlayer = 'b';
			lblCurrPlayer.setText("Black Player's Turn.");
			lblCurrPlayer.setBackground(Color.BLACK);
			lblCurrPlayer.setForeground(Color.WHITE);
		} else if (currPlayer == 'b' || currPlayer == ' ') {
			currPlayer = 'w';
			lblCurrPlayer.setText("White Player's Turn.");
			lblCurrPlayer.setBackground(Color.WHITE);
			lblCurrPlayer.setForeground(Color.BLACK);
		}
	}

	private void clickCell(java.awt.event.MouseEvent e, int x, int y, int i) {
		JLabel lblClicked = (JLabel) e.getSource();
		if (heldI != -1) // We're dropping a piece
		{
			jContentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			movePiece(heldX, heldY, heldI, x, y, i, false);
			heldX = heldY = heldI = -1;
		} else // We're picking up a piece
		{
			if (jPieces[x][y] == null || jPieces[x][y].equals("")) {
				lblStatus.setText("No piece to pick up.");
			} else {
				jContentPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				lblClicked.setBackground(new Color(255, 176, 70));
				lblStatus.setText("Picked up " + pieceName.get(jPieces[x][y]) + " from " + showBoardRelative(x, y));
				heldX = x;
				heldY = y;
				heldI = i;
			}
		}
	}

	/**
	 * This method initializes tlbMain
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getTlbMain() {
		if (tlbMain == null) {
			lblCurrPlayer = new JLabel();
			lblCurrPlayer.setText("");
			lblCurrPlayer.setOpaque(true);
			lblStatus = new JLabel();
			lblStatus.setText("");
			lblStatus.setPreferredSize(new Dimension(200, 16));
			lblStatus.setSize(new Dimension(200, 16));
			tlbMain = new JToolBar();
			tlbMain.setOrientation(JToolBar.HORIZONTAL);
			tlbMain.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			tlbMain.setFloatable(false);
			tlbMain.add(getBtnNewGame());
			tlbMain.add(new JToolBar.Separator());
			tlbMain.add(getBtnUndo());
			tlbMain.add(new JToolBar.Separator());
			tlbMain.add(lblCurrPlayer);
			tlbMain.add(new JToolBar.Separator());
			tlbMain.add(lblStatus);
		}
		return tlbMain;
	}

	/**
	 * This method initializes btnNewGame
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnNewGame() {
		if (btnNewGame == null) {
			btnNewGame = new JButton();
			btnNewGame.setText("New Game");
			btnNewGame.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseReleased(java.awt.event.MouseEvent e) {
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Are you sure you wish to end this game?")) {
						newGame();
					}
				}
			});
		}
		return btnNewGame;
	}

}