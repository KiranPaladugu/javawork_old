package jTable;

import javax.swing.table.*;

/** A custom DefaultTableModel that returns the class
 *  type for the default cell renderers to use. The user is 
 *  restricted to editing only the Comment and Visited columns.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted.
 */

public class CustomTableModel extends DefaultTableModel {
  
  public Class getColumnClass(int column) {
    return(getValueAt(0, column).getClass());
  }

  // Only permit edit of "Comment" and "Visited" columns.
  public boolean isCellEditable(int row, int column) {
    return(column==3 || column==4);
  }
}