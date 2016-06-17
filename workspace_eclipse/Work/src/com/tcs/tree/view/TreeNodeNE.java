package com.tcs.tree.view;

import com.marconi.fusion.X36.X36NetworkElement;

public class TreeNodeNE extends AbstractSetTypeUserTreeNode{

	private X36NetworkElement ne;

	public TreeNodeNE(X36NetworkElement ne) {
		super(ne);
		this.ne = ne;
	}

	@Override
	public String getName() {
		if(ne != null){
			return "NeId : "+ne.getNeId().getValue();
		}
		return null;
	}

	@Override
	public int getId() {
		if(ne!=null){
			return ne.getNeId().getValue();
		}
		return 0;
	}


	@Override
	public String getSpecificInfo() {
		if(ne!=null && ne.getNeInfo()!=null){
			return ne.getNeInfo().toString();
		}
		return undefined;
	}

}
