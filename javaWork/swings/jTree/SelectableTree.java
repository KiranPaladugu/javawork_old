package jTree;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/** JTree that reports selections by placing their string values
 *  in a JTextField.
 *
 *  Taken from Core Web Programming from 
 *  Prentice Hall and Sun Microsystems Press,
 *  http://www.corewebprogramming.com/.
 *  &copy; 2001 Marty Hall and Larry Brown;
 *  may be freely used or adapted. 
 */

public class SelectableTree extends JFrame
                            implements TreeSelectionListener {
  public static void main(String[] args) {
    new SelectableTree();
  }

  private JTree tree;
  private JTextField currentSelectionField;
  
  public SelectableTree() {
    super("JTree Selections");
    WindowUtilities.setNativeLookAndFeel();
    addWindowListener(new ExitListener());
    Container content = getContentPane();
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
          new DefaultMutableTreeNode("Grandchild " + childIndex +
                                     "." + grandChildIndex);
        child.add(grandChild);
      }
    }
    tree = new JTree(root);
    tree.addTreeSelectionListener(this);
    content.add(new JScrollPane(tree), BorderLayout.CENTER);
    currentSelectionField = 
      new JTextField("Current Selection: NONE");
    content.add(currentSelectionField, BorderLayout.SOUTH);
    setSize(250, 275);
    setVisible(true);
  }

  public void valueChanged(TreeSelectionEvent event) {
    Object selection = tree.getLastSelectedPathComponent();
    if (selection != null) {
      currentSelectionField.setText
        ("Current Selection: " + selection.toString());
    }
  }
}