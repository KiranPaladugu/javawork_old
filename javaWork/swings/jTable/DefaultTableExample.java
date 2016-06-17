package jTable;

import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;

/** JTable that uses the DefaultTableModel, which permits
 *  adding rows and columns programmatically.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted.
 */

public class DefaultTableExample extends JTable {
  
  private String[] columnNames = 
    { "Flag", "City", "Country", "Comment", "Visited" };

  public DefaultTableExample() {
    this(new DefaultTableModel());
  }
    
  public DefaultTableExample(DefaultTableModel model) {
    super(model);
    
    JavaLocationCollection collection =
      new JavaLocationCollection();
    JavaLocation[] locations = collection.getLocations();

    // Set up the column labels and data for the table model.
    int i;
    for(i=0; i<columnNames.length; i++ ) {
      model.addColumn(columnNames[i]); 
    }
    for(i=0; i<locations.length; i++) {
      model.addRow(getRowData(locations[i]));
    }
  }

  private Vector getRowData(JavaLocation location) {
    Vector vector = new Vector();
    vector.add(new ImageIcon(location.getFlagFile()));
    vector.add("Java");
    vector.add(location.getCountry());
    vector.add(location.getComment());
    vector.add(new Boolean(false));
    return(vector);
  }
  
  public static void main(String[] args) {
    WindowUtilities.setNativeLookAndFeel();    
    WindowUtilities.openInJFrame(
       new JScrollPane(new DefaultTableExample()), 600, 150, 
                       "Using a DefaultTableModel");
  }
}