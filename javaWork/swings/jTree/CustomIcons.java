package jTree;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/** JTree with missing or custom icons at the tree nodes. 
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted.
 */

public class CustomIcons extends JFrame {
  public static void main(String[] args) {
    new CustomIcons();
  }

  private Icon customOpenIcon = 
            new ImageIcon("images/Circle_1.gif");
  private Icon customClosedIcon = 
            new ImageIcon("images/Circle_2.gif");
  private Icon customLeafIcon = 
            new ImageIcon("images/Circle_3.gif");
  
  public CustomIcons() {
    super("JTree Selections");
    WindowUtilities.setNativeLookAndFeel();
    addWindowListener(new ExitListener());
    Container content = getContentPane();
    content.setLayout(new FlowLayout());
    DefaultMutableTreeNode root =
      new DefaultMutableTreeNode("Root");
    DefaultMutableTreeNode child;
    DefaultMutableTreeNode grandChild;
    for(int childIndex=1; childIndex<4; childIndex++) {
      child = new DefaultMutableTreeNode("Child " + childIndex);
      root.add(child);
      for(int grandChildIndex=1; grandChildIndex<4; 
                                 grandChildIndex++) {
        grandChild =
          new DefaultMutableTreeNode("Grandchild " + 
                                     childIndex +
                                     "." + grandChildIndex);
        child.add(grandChild);
      }
    }

    JTree tree1 = new JTree(root);
    tree1.expandRow(1); // Expand children to illustrate leaf icons.
    JScrollPane pane1 = new JScrollPane(tree1);
    pane1.setBorder(
            BorderFactory.createTitledBorder("Standard Icons"));
    content.add(pane1);

    JTree tree2 = new JTree(root);
    // Expand children to illustrate leaf icons.
    tree2.expandRow(2); 
    DefaultTreeCellRenderer renderer2 = 
                              new DefaultTreeCellRenderer();
    renderer2.setOpenIcon(null);
    renderer2.setClosedIcon(null);
    renderer2.setLeafIcon(null);
    tree2.setCellRenderer(renderer2);
    JScrollPane pane2 = new JScrollPane(tree2);
    pane2.setBorder(
            BorderFactory.createTitledBorder("No Icons"));
    content.add(pane2);

    JTree tree3 = new JTree(root);
    // Expand children to illustrate leaf icons.
    tree3.expandRow(3); 
    DefaultTreeCellRenderer renderer3 = 
                              new DefaultTreeCellRenderer();
    renderer3.setOpenIcon(customOpenIcon);
    renderer3.setClosedIcon(customClosedIcon);
    renderer3.setLeafIcon(customLeafIcon);
    tree3.setCellRenderer(renderer3);
    JScrollPane pane3 = new JScrollPane(tree3);
    pane3.setBorder(
           BorderFactory.createTitledBorder("Custom Icons"));
    content.add(pane3);

    pack();
    setVisible(true);
  }
}