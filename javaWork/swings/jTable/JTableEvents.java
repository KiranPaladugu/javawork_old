package jTable;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/** A JTable that responds to TableModelEvents and
 *  updates other cells in the table, based on user input.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted.
 */

public class JTableEvents extends JFrame {
  private final int COL_COST     = 1;
  private final int COL_QTY      = 2;
  private final int COL_TOTAL    = 3;
  private final int ROW_LAST     = 5;
  private DecimalFormat df = new DecimalFormat("$####.##");
  private JTable sampleJTable;
  private DefaultTableModel tableModel;

  public static void main(String[] args) {
    new JTableEvents();
  }

  public JTableEvents() {
    super("Using TableEvents");
    WindowUtilities.setNativeLookAndFeel();
    addWindowListener(new ExitListener());
    Container content = getContentPane();

    String[] columnNames = { "Book", "Cost", "Qty", "Total" };

    final Object[][] data = {
      {"Core Web Programming", "$ 0.99", "0", "$0.00"},
      {"Core Servlets and JavaServer Pages",
                               "$34.39", "0", "$0.00"},
      {"Core Swing",           "$39.99", "0", "$0.00"},
      {"Core Java, Volume I",  "$31.49", "0", "$0.00"},
      {"Core Java, Volume II", "$34.39", "0", "$0.00"},
      {null, null,                  "Grand:", "$0.00"} };

    tableModel = new DefaultTableModel(data, columnNames);
    tableModel.addTableModelListener(
      new TableModelListener() {
        int row, col;
        int quantity;
        float cost, subTotal, grandTotal;
        public void tableChanged(TableModelEvent event) {
          row = event.getFirstRow();
          col = event.getColumn();
          // Only update table if a new book quantity entered.
          if (col == COL_QTY) {
            try {
              cost = getFormattedCellValue(row, COL_COST);
              quantity = (int)getFormattedCellValue(row, COL_QTY);
              subTotal = quantity * cost;

              // Update row total.
              tableModel.setValueAt(df.format(subTotal),
                                    row, COL_TOTAL);
              // Update grand total.
              grandTotal =0;
              for(int row=0; row<data.length-1; row++) {
                grandTotal += getFormattedCellValue(row, COL_TOTAL);
              }
              tableModel.setValueAt(df.format(grandTotal),
                                    ROW_LAST,COL_TOTAL);
              tableModel.fireTableDataChanged();
            } catch (NumberFormatException nfe) {
                // Send error message to user.
                JOptionPane.showMessageDialog(
                             JTableEvents.this,
                             "Illegal value entered!");
            }
          }
        }

        private float getFormattedCellValue(int row, int col) {
          String value = (String)tableModel.getValueAt(row, col);
          return(Float.parseFloat(value.replace('$',' ')));
        }
      });

    sampleJTable = new JTable(tableModel);
    setColumnAlignment(sampleJTable.getColumnModel());
    JScrollPane tablePane = new JScrollPane(sampleJTable);

    content.add(tablePane, BorderLayout.CENTER);
    setSize(460,150);
    setVisible(true);
  }

  // Right-align all but the first column.
  private void setColumnAlignment(TableColumnModel tcm) {
    TableColumn column;
    DefaultTableCellRenderer renderer =
      new DefaultTableCellRenderer();
    for(int i=1; i<tcm.getColumnCount(); i++) {
      column = tcm.getColumn(i);
      renderer.setHorizontalAlignment(SwingConstants.RIGHT);
      column.setCellRenderer(renderer);
    }
  }
}