package jTable;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/** Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted.
 */

// Final version of FileTransfer. Modification of the 
// label is thread safe.

public class FileTransfer extends Thread {
  private String filename;
  private JLabel label;

  public FileTransfer(String filename, JLabel label) {
    this.filename = filename;
    this.label = label;
  }

  public void run() {
  
    try {
      // Place the runnable object to update the label
      // on the event queue. The invokeAndWait method
      // will block until the label is updated.
      SwingUtilities.invokeAndWait(
        new Runnable() {
          public void run() {
            label.setText("Transferring " + filename);
          }
        });
    } catch(InvocationTargetException ite) {
    } catch(InterruptedException ie) { }
       
    // Transfer file to server. Lengthy process.
    //doTransfer(...);

    // Perform the final update to the label from
    // within the runnable object. Use invokeLater;
    // blocking is not necessary.
    SwingUtilities.invokeLater(
       new Runnable() {
         public void run() {
           label.setText("Transfer completed");
         }
       });
  }
}