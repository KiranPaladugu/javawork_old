package com.tcs.berReader.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.framework.reg.Register;
import com.marconi.fusion.X36.X36AlarmDetailReport;
import com.marconi.fusion.X36.X36CardInformation;
import com.marconi.fusion.X36.X36CrossConnection;
import com.marconi.fusion.X36.X36GetReportNodeCrossConnections;
import com.marconi.fusion.X36.X36GetReportProvisioning;
import com.marconi.fusion.X36.X36PortInformation;
import com.marconi.fusion.X36.X36SetOfAlEvent;
import com.marconi.fusion.X36.X36SetOfCardInformation;
import com.marconi.fusion.X36.X36SetOfCrossConnection;
import com.marconi.fusion.X36.X36SetOfPortInformation;
import com.marconi.fusion.X36.X36SetOfShelfInformation;
import com.marconi.fusion.X36.X36ShelfInformation;
import com.tcs.berReader.BERFileData;
import com.tcs.tree.view.AbstractSetTypeUserTreeNode;
import com.tcs.tree.view.TreeNodeAlarm;
import com.tcs.tree.view.TreeNodeCard;
import com.tcs.tree.view.TreeNodeCellRenderer;
import com.tcs.tree.view.TreeNodeCrossConnection;
import com.tcs.tree.view.TreeNodeNE;
import com.tcs.tree.view.TreeNodePort;
import com.tcs.tree.view.TreeNodeShelf;
import com.tcs.tree.view.UserTreeNode;

public class BERFileDisplay {
	private BERFileData data;
	private JMenuItem displyObject;
	private JMenuItem getSpecificInfo;
	private JMenuItem getMember, getAllMemberNames;
	private JTree tree;
	private JTextArea area;

	public BERFileDisplay(BERFileData data) {
		this.data = data;
	}

	public void displayBer() {
		if (true) {
			treeViewDialog();
			return;
		}
	}

	public void treeViewDialog() {
		JDialog dialog = new JDialog(((UserInterface) Register.getObject(UserInterface.class)).getFrame());
		dialog.setLayout(new GridLayout(1, 1));
		dialog.add(getTreeViewPanel());
		dialog.setBounds(DisplayDialog.setDialogLocation(300, 600));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

	}

	public JPanel getTreeViewPanel() {
		JPanel dialog = new JPanel();
		area = new JTextArea();
		area.setWrapStyleWord(true);
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem selectAll = new JMenuItem("SelectAll");
		selectAll.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				area.selectAll();
			}
		});
		JMenuItem copy = new JMenuItem("CopySelected");
		copy.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				area.copy();
			}
		});
		JMenuItem copyAll = new JMenuItem("CopyAll");
		copyAll.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int x = area.getSelectionStart();
				int y = area.getSelectionEnd();				
				area.selectAll();
				area.copy();
				if(x!=-1 && y!=-1){
					area.select(x, y);
				}
			}
		});
		
		popup.add(selectAll);
		popup.addSeparator();
		popup.add(copy);
		popup.add(copyAll);
		area.addMouseListener(new MouseListener() {
			
			
			public void mouseReleased(MouseEvent arg0) {
				if(arg0.isPopupTrigger()){
					popup.show(area, arg0.getX(), arg0.getY());
				}
			}
			
			
			public void mousePressed(MouseEvent arg0) {
				
			}
			
			
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		dialog.setVisible(true);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		tree = new JTree(root);
		tree.setToolTipText("TreeView");
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			
			public void valueChanged(TreeSelectionEvent arg0) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					Object object = tree.getSelectionPath().getLastPathComponent();
					if (object instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
						Object obj = node.getUserObject();
						if (obj instanceof UserTreeNode) {
							if (obj instanceof AbstractSetTypeUserTreeNode) {
								area.setText(((AbstractSetTypeUserTreeNode) obj).getSyntax());
								area.setCaretPosition(0);
								tree.setToolTipText(obj.toString());
							} else {
								area.setText("UNDEFINED");
								tree.setToolTipText("TreeView");
							}
						} else {
							if (obj.equals("NodeCconfiguration")) {
								if (data.getNodeConfig() != null)
									area.setText(data.getNodeConfig().toString());
								area.setCaretPosition(0);
								tree.setToolTipText("NodeCconfiguration");
							} else {
								area.setText("UNDEFINED");
								tree.setToolTipText("TreeView");
							}
						}
					} else {
						area.setText("UNDEFINED");
						tree.setToolTipText("TreeView");
					}
				} else {
					area.setText("UNDEFINED");
					tree.setToolTipText("TreeView");
				}
			}
		});

		final JPopupMenu treePopup = initPopup();
		tree.add(treePopup);
		tree.setCellRenderer(new TreeNodeCellRenderer());
		tree.addMouseListener(new MouseListener() {

			
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					treePopup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			
			public void mousePressed(MouseEvent arg0) {

			}

			
			public void mouseExited(MouseEvent arg0) {

			}

			
			public void mouseEntered(MouseEvent arg0) {

			}

			
			public void mouseClicked(MouseEvent arg0) {

			}
		});
		dialog.setLayout(new GridLayout());
		JScrollPane scroll = new JScrollPane(tree);
		JScrollPane areaScroll = new JScrollPane(area);
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, areaScroll);
		split.setDividerLocation(250);
		split.setDividerSize(2);
		dialog.add(split);

		DefaultMutableTreeNode nodeName = new DefaultMutableTreeNode("NODE");
		// NE inforamtion.
		if (data != null) {
			if (data.getNodeConfig() != null) {
				TreeNodeNE ne = new TreeNodeNE(data.getNodeConfig().getBody().getNetworkElement());
				nodeName = new DefaultMutableTreeNode(ne);

			}
		} else {
			return dialog;
		}
		root.add(nodeName);
		tree.makeVisible(new TreePath(root.getFirstLeaf().getPath()));
		// NodeCOnfiguration.

		DefaultMutableTreeNode node = new DefaultMutableTreeNode("NodeCconfiguration");
		NodeConfigQueryHandler ncq = new NodeConfigQueryHandler(data);
		X36SetOfShelfInformation shelfs = ncq.getShelfInfo();
		if (shelfs != null && shelfs.size() > 0) {
			for (X36ShelfInformation shelf : shelfs) {
				TreeNodeShelf s = new TreeNodeShelf(shelf);
				DefaultMutableTreeNode sh = new DefaultMutableTreeNode(s);
				node.add(sh);
				X36SetOfCardInformation cards = shelf.getCards();
				for (X36CardInformation card : cards) {
					TreeNodeCard c = new TreeNodeCard(card);
					DefaultMutableTreeNode ca = new DefaultMutableTreeNode(c);
					sh.add(ca);
					X36SetOfPortInformation ports = card.getPorts();
					for (X36PortInformation port : ports) {
						TreeNodePort p = new TreeNodePort(port);
						DefaultMutableTreeNode po = new DefaultMutableTreeNode(p);
						ca.add(po);
					}
				}
			}
			nodeName.add(node);
		}

		// CrossConnections

		DefaultMutableTreeNode xC = new DefaultMutableTreeNode("CrossConnections");
		if (data.getNodeCrossConnections() != null) {
			X36GetReportNodeCrossConnections xCs = data.getNodeCrossConnections().getBody();
			X36SetOfCrossConnection xconns = xCs.getCrossConnections();
			if (xconns.size() > 0) {
				for (X36CrossConnection xconn : xconns) {
					TreeNodeCrossConnection conn = new TreeNodeCrossConnection(xconn);
					DefaultMutableTreeNode tconn = new DefaultMutableTreeNode(conn);
					xC.add(tconn);
				}
				nodeName.add(xC);
			}
		}

		// Alarms and Events

		DefaultMutableTreeNode alarms = new DefaultMutableTreeNode("NodeAlarms");
		if (data.getProvisioning() != null) {
			X36GetReportProvisioning alarmsInfo = data.getProvisioning().getBody();
			X36SetOfAlEvent events = alarmsInfo.getAlEventList();
			if (events.size() > 0) {
				for (X36AlarmDetailReport event : events) {
					TreeNodeAlarm alarm = new TreeNodeAlarm(event);
					DefaultMutableTreeNode al = new DefaultMutableTreeNode(alarm);
					alarms.add(al);
				}
				nodeName.add(alarms);
			}
		}

		return dialog;

	}

	private JPopupMenu initPopup() {
		JPopupMenu popup = new JPopupMenu();
		displyObject = new JMenuItem("GetDetails");
		getSpecificInfo = new JMenuItem("GetSpecificInfo");
		getMember = new JMenuItem("GetMember");
		getAllMemberNames = new JMenuItem("GetAllMemberNames");
		displyObject.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent ae) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					Object object = tree.getSelectionPath().getLastPathComponent();
					if (object instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
						Object obj = node.getUserObject();
						if (obj instanceof UserTreeNode) {
							if (obj instanceof AbstractSetTypeUserTreeNode) {
								DisplayDialog.dispalyMessage(((AbstractSetTypeUserTreeNode) obj).getSyntax(),
										"Network Element Details!");
							}
						}
					}

				}
			}
		});
		getSpecificInfo.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent arg0) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					Object object = tree.getSelectionPath().getLastPathComponent();
					if (object instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
						Object obj = node.getUserObject();
						if (obj instanceof UserTreeNode) {
							if (obj instanceof AbstractSetTypeUserTreeNode) {
								DisplayDialog.dispalyMessage(((AbstractSetTypeUserTreeNode) obj).getSpecificInfo(),
										"Network Element Details!");
							}
						}
					}
				}
			}
		});

		getAllMemberNames.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					Object object = tree.getSelectionPath().getLastPathComponent();
					if (object instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
						Object obj = node.getUserObject();
						if (obj instanceof UserTreeNode) {
							if (obj instanceof AbstractSetTypeUserTreeNode) {
								String str[] = ((AbstractSetTypeUserTreeNode) obj).getMembers();
								if (str != null) {
									StringBuilder msg = new StringBuilder("");
									msg.append("===========================================\n");
									msg.append("	ID	| MemberName\n");
									msg.append("-------------------------------------------\n");
									for (int i = 0; i < str.length; i++) {
										msg.append("	" + i + "\t: " + str[i] + "\n");
									}
									msg.append("===========================================\n");
									DisplayDialog.dispalyMessage(msg.toString(), "Members Details!");
								} else {
									MessageHandler.displayErrorMessage("Unable to perform operation. \nPlease try again");
								}
							}
						}
					}
				}
			}
		});

		getMember.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent arg0) {
				String str = MessageHandler.dispalyInputMessage("Please Enter Memeber id or Name");
				if(str == null){
					return;
				}
				if ( str.trim().equals("")) {
					MessageHandler.displayErrorMessage("Invalid Input!");
					return;
				}
				str = str.trim();
				int memId = -1;
				try {
					memId = Integer.parseInt(str);
				} catch (Throwable t) {
					memId = -1;
				}

				TreePath path = tree.getSelectionPath();
				if (path != null) {
					Object object = tree.getSelectionPath().getLastPathComponent();
					if (object instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
						Object obj = node.getUserObject();
						if (obj instanceof UserTreeNode) {
							if (obj instanceof AbstractSetTypeUserTreeNode) {
								String msg = null;
								((AbstractSetTypeUserTreeNode) obj).getMembers();
								if (memId != -1) {
									msg = ((AbstractSetTypeUserTreeNode) obj).getMember(memId);
								} else {
									msg = ((AbstractSetTypeUserTreeNode) obj).getMember(str);
								}
								if (msg == null) {
									msg = "Member with ID / Name: "+str+" Not found!";
								}
								DisplayDialog.dispalyMessage(msg, "Member " + str + " Details!");
							}
						}
					}
				}
			}
		});

		popup.add(displyObject);
		popup.add(getSpecificInfo);
		popup.addSeparator();
		popup.add(getAllMemberNames);
		popup.add(getMember);
		return popup;
	}
}
