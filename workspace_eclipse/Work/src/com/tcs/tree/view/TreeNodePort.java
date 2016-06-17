package com.tcs.tree.view;

import com.marconi.fusion.X36.X36PortInformation;

public class TreeNodePort extends AbstractSetTypeUserTreeNode{

	private X36PortInformation port;
	
	public TreeNodePort(X36PortInformation port) {
		super(port);
		this.port = port;
	}

	@Override
	public String getName() {
		if(port != null && port.getPortId()!=null)
		return "Port:"+port.getPortId().getValue();
		else return undefined;
	}

	@Override
	public int getId() {
		if(port !=null && port.getPortId() !=null)
		return port.getPortId().getValue();
		else return 0;
	}

	@Override
	public String getSpecificInfo() {
		if(port!=null && port.getSpecificPortInformation()!=null)
		return port.getSpecificPortInformation().toString();
		else return undefined;
	}
}
