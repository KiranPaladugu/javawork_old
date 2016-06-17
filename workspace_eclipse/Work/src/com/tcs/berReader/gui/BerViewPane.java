package com.tcs.berReader.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.framework.reg.Register;
import com.framework.reg.RegisterException;
import com.marconi.fusion.X36.X36MsgGetReportBshrProtection;
import com.marconi.fusion.X36.X36MsgGetReportNodeConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportNodeCrossConnections;
import com.marconi.fusion.X36.X36MsgGetReportNodePossibleConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportProtectionConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportProvisioning;
import com.marconi.fusion.X36.X36MsgReportGetCollection;
import com.marconi.fusion.X36.X36MsgReportGetDataProfiles;
import com.tcs.berReader.BERFileData;

public class BerViewPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8602771307713812575L;
	private JTabbedPane tabber;
	private BERFileData berData;
	private NodeConfigPopup nodeConfigPopup;

	public BerViewPane(BERFileData data) {
		Register.register(this);
		this.berData = data;
		setLayout(new GridLayout(1, 1));
		init();
	}

	private void init() {
		initTabber();
		if (berData == null) {
			displayEmpty();
		} else {
			displayBer();
		}
	}

	public void displayBER(BERFileData data) {
		this.berData = data;
		if (berData != null) {
			displayBer();
		}
	}

	private void initTabber() {
		removeAll();
		tabber = new JTabbedPane();
		add(tabber);
	}

	private void displayEmpty() {
		JTextArea area = new JTextArea();
		// area.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		tabber.addTab("Loading ..... ", area);
	}

	private void displayBer() {
		initTabber();
		if ((ApplicationContext.getApplicationProperties().getBoolProperty("Application.BERViewer.TreeView", true))) {
			displayTreeView();
		}
		if((ApplicationContext.getApplicationProperties().getBoolProperty("Application.BERViewer.MessageInTabs", true))){
		displayNodeconfig();
		displayNodePossibleConfig();
		displayNodeDataProfiles();
		displayNodeCrossConnections();
		displayNodeProtectionConfiguration();
		displayNodeCollection();
		displayNodeBshrProtection();
		dispalyProvisioning();
		}
	}

	private void displayTreeView() {
		try {
			BERFileDisplay display = new BERFileDisplay(berData);
			JPanel panel = display.getTreeViewPanel();
			tabber.addTab("TreeView", panel);
		} catch (Exception e) {
		}

	}

	private void displayNodeBshrProtection() {
		X36MsgGetReportBshrProtection report = berData.getBshrProtection();
		if (report != null) {
			addTab("NodeBSHRProtection", report.getBody().toString());
		}

	}

	private void displayNodeCollection() {
		X36MsgReportGetCollection report = berData.getCollection();
		if (report != null) {
			addTab("NodeCollection", report.getBody().toString());
		}

	}

	private void displayNodeProtectionConfiguration() {
		X36MsgGetReportProtectionConfiguration report = berData.getProtectionConfig();
		if (report != null) {
			addTab("NodeProtectionConfiguration", report.getBody().toString());
		}

	}

	private void displayNodeCrossConnections() {
		X36MsgGetReportNodeCrossConnections report = berData.getNodeCrossConnections();
		if (report != null) {
			addTab("NodeCrossConnections", report.getBody().toString());
		}

	}

	private void addTab(String name, String message) {
		final JTextArea area = new JTextArea();
		JScrollPane scorll = new JScrollPane(area);
		area.setText(message);
		area.setEditable(false);
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
		tabber.addTab(name, scorll);
	}

	private void displayNodeDataProfiles() {
		X36MsgReportGetDataProfiles report = berData.getDataProfiles();
		if (report != null) {
			addTab("NodeDataProfiles", report.getBody().toString());
		}

	}

	private void dispalyProvisioning() {
		X36MsgGetReportProvisioning report = berData.getProvisioning();
		if (report != null) {
			addTab("NodeProvisioning", report.getBody().toString());
		}

	}

	private void displayNodePossibleConfig() {
		X36MsgGetReportNodePossibleConfiguration report = berData.getNodePossibleConfig();
		if (report != null) {
			addTab("NodePossibleConfiguration", report.getBody().toString());
		}

	}

	private void displayNodeconfig() {
		X36MsgGetReportNodeConfiguration report = berData.getNodeConfig();
		if (report != null) {
			JTextArea area = new JTextArea();
			area.setText(report.getBody().toString());
			area.setWrapStyleWord(true);
			JScrollPane scorll = new JScrollPane(area);
			tabber.addTab("NodeConfiguration", scorll);
			try {
				nodeConfigPopup = (NodeConfigPopup) Register.getCheckedObject(NodeConfigPopup.class);
			} catch (RegisterException e) {
				e.printStackTrace();
			}
			if (nodeConfigPopup != null) {
				area.add(nodeConfigPopup);
				area.addMouseListener(new MouseListener() {

					
					public void mouseReleased(MouseEvent e) {
						if (e.isPopupTrigger()) {
							nodeConfigPopup.show(e.getComponent(), e.getX(), e.getY());
						}
					}

					
					public void mousePressed(MouseEvent e) {

					}

					
					public void mouseExited(MouseEvent arg0) {

					}

					
					public void mouseEntered(MouseEvent arg0) {

					}

					
					public void mouseClicked(MouseEvent arg0) {

					}
				});
			}

		}
	}

}
