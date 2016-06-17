package com.tcs.tree.view;

import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.tcs.ber.resource.PropertyFinder;
import com.tcs.ber.resource.ResourceFinder;

public class TreeNodeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8891785885994848537L;

	public java.awt.Component getTreeCellRendererComponent( JTree tree,
                                             Object value,
                                             boolean bSelected,
                                             boolean bExpanded,
                                             boolean bLeaf,
                                             int iRow,
                                             boolean bHasFocus ) {
		super.getTreeCellRendererComponent(tree, value, bSelected, bExpanded, bLeaf, iRow, bHasFocus);
		
        // Find out which node we are rendering and get its text		
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object obj = node.getUserObject();
        if(obj instanceof TreeNodeNE){
			try {
				setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.NE")));
			} catch (IOException e) {
				
			}
		}else if(obj instanceof TreeNodePort){
			try {
				setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.PORT")));
			} catch (IOException e) {
				
			}
		}else if(obj instanceof TreeNodeCard){
			try {
				setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.CARD")));
			} catch (IOException e) {
				
			}
		}else if(obj instanceof TreeNodeAlarm){
			try {
				setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.ALARM")));
			} catch (IOException e) {
				
			}
		}else if(obj instanceof TreeNodeShelf){
			try {
				setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.SHELF")));
			} catch (IOException e) {
				
			}
		}else if(obj instanceof TreeNodeCrossConnection){
			try {
				setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.XCONNECTION")));
			} catch (IOException e) {
				
			}
		}else{
			if(obj.equals("Root")){
				try {
					setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.ROOT")));
				} catch (IOException e) {
					
				}
			}else{
				try {
					setIcon(ResourceFinder.getImageWithName(PropertyFinder.getPropertyWithName("BERViewer.Icon.name.OTHER")));
				} catch (IOException e) {
					
				}
			}
		}
        return this;
    }
}