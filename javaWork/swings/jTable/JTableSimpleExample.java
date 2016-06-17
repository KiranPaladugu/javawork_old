package jTable;

import java.awt.*;
import javax.swing.*;

/** Simple JTable example that uses a String array for the
 *  table header and table data.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted.
 */

public class JTableSimpleExample extends JFrame {
  public static void main(String[] args) {
    new JTableSimpleExample();
  }

  private final int COLUMNS = 4;
  private final int ROWS = 15;
  private JTable sampleJTable;

  public JTableSimpleExample() {
    super("Creating a Simple JTable");
    WindowUtilities.setNativeLookAndFeel();
    addWindowListener(new ExitListener());
    Container content = getContentPane();

    String[]   columnNames = buildColumnNames(COLUMNS);
    String[][] tableCells = buildTableCells(ROWS, COLUMNS);

    sampleJTable = new JTable(tableCells, columnNames);
    JScrollPane tablePane = new JScrollPane(sampleJTable);
    content.add(tablePane, BorderLayout.CENTER);
    setSize(450,150);
    setVisible(true);
  }

  private String[] buildColumnNames(int columns) {
    String[] header = new String[columns];
    for(int i=0; i<columns; i++) {
      header[i] = "Column " + i;
    }
    return(header);
  }

  private String[][] buildTableCells(int rows, int columns) {
    String[][] cells = new String[rows][columns];
    for(int i=0; i<rows ; i++) {
      for(int j=0; j<columns; j++ ) {
        cells[i][j] = "Row " + i + ", Col " + j;
      }
    }
    return(cells);
  }
}