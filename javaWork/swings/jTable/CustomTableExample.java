package jTable;

import javax.swing.JScrollPane;
import javax.swing.JTable;

/** JTable that uses a CustomTableModel to correctly render
 *  the table cells that contain images and boolean values.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted.
 */

public class CustomTableExample extends DefaultTableExample {
  
  public CustomTableExample() {
    super(new CustomTableModel());
    setCellSizes();
  }
    
  private void setCellSizes() {
    setRowHeight(50);
    getColumn("Flag").setMaxWidth(55);
    getColumn("City").setPreferredWidth(60);
    getColumn("Country").setMinWidth(80);
    getColumn("Comment").setMinWidth(150);
    // Call to resize columns in viewport (bug).
    sizeColumnsToFit(JTable.AUTO_RESIZE_OFF);
  }
  
  public static void main(String[] args) {
    WindowUtilities.setNativeLookAndFeel();
    WindowUtilities.openInJFrame(
       new JScrollPane(new CustomTableExample()), 525, 255, 
                       "Using a CustomTableModel");
  }
}