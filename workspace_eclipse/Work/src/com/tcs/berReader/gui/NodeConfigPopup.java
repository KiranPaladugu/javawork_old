package com.tcs.berReader.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.framework.reg.Register;

public class NodeConfigPopup extends JPopupMenu{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4937795164301735145L;
	private JMenuItem getNetworkElement,getCardInfo , getPortInfo;
	private BERListPane list;
	private NodeConfigQueryHandler ncq;
	
	public NodeConfigPopup() {
		init();
		list = (BERListPane) Register.getObject(BERListPane.class);
		ncq = new NodeConfigQueryHandler(list.getSelectedItem());
	}
	private void init(){
		getNetworkElement = new JMenuItem("getNetworkElement.");
		add(getNetworkElement);
		getNetworkElement.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent arg0) {				
				DisplayDialog.dispaly(ncq.getNetwokDetails().toString());
			}
		});
		
		getCardInfo = new JMenuItem("getCardInfo");
		getCardInfo.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent arg0) {				
				
			}
		});
		add(getCardInfo);
		
		getPortInfo = new JMenuItem("getPortInfo");
		getPortInfo.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent arg0) {
				DisplayDialog.showLocationInputDialog(ncq);
//				LocationInfo loc = DisplayDialog.getLocaionInfo();
//				X36PortInformation port = ncq.getPortInfo(loc);
//				if(port == null ){
//					MessageHandler.displayErrorMessage("Unable to obtain Required Information.");
//				}else{
//					DisplayDialog.dispaly(port.toString());
//				}
				
			}
		});
		add(getPortInfo);
		
	}
	

}
