package jTree;

import java.awt.*;
import javax.swing.*;

/** Example tree that builds child nodes on the fly.
 *  See OutlineNode for details.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted. 
 */

public class DynamicTree extends JFrame {
  public static void main(String[] args) {
    int n = 5; // Number of children to give each node.
    if (args.length > 0) {
      try {
        n = Integer.parseInt(args[0]);
      } catch(NumberFormatException nfe) {
        System.out.println(
          "Can't parse number; using default of " + n);
      }
   }
    new DynamicTree(n);
  }
 
  public DynamicTree(int n) {
    super("Creating a Dynamic JTree");
    WindowUtilities.setNativeLookAndFeel();
    addWindowListener(new ExitListener());
    Container content = getContentPane();
    JTree tree = new JTree(new OutlineNode(1, n));
    content.add(new JScrollPane(tree), BorderLayout.CENTER);
    setSize(300, 475);
    setVisible(true);
  }
}