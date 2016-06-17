package com.tcs.tree.view;

import com.marconi.fusion.X36.X36CrossConnection;
import com.marconi.fusion.X36.X36FlowPoint;
import com.marconi.fusion.X36.X36SetOfFlowPoints;

public class TreeNodeCrossConnection extends AbstractSetTypeUserTreeNode{

	private X36CrossConnection xconn;

	public TreeNodeCrossConnection(X36CrossConnection xconn) {
		super(xconn);
		this.xconn = xconn;
	}
	@Override
	public String getSyntax() {
		if(xconn != null)
			return xconn.toString();
		return undefined;
	}

	@Override
	public String getNormalizedData() {
		
		if(xconn != null){
			try{
				return xconn.format();
			}catch (Exception e) {
			}
		}
		return undefined;
	}

	@Override
	public String getName() {
		if (xconn != null) {
			if (xconn.getSpecificCrossConnection().isData()) {
				X36SetOfFlowPoints fps = xconn.getSpecificCrossConnection().getData().getFlowPointDetails();
				StringBuilder sb = new StringBuilder("[Data CrossConnection]{");
				int n=1;
				for (X36FlowPoint fp : fps) {
					sb.append("Shelf:" + fp.getPortIdentity().getShelfId().getValue());
					sb.append("\\Card:" + fp.getPortIdentity().getCardId().getValue());
					sb.append("\\Port:" + fp.getPortIdentity().getPortId().getValue());
					if(n!=fps.size()){
						sb.append(",");
						n++;
					}
				}
				sb.append("}");
				return sb.toString();
			} else
				return "Cross-Connection";
		}
		return undefined;
	}

	@Override
	public int getId() {
		if(xconn!= null)
			return 1;
		return 0;
	}

	@Override
	public String getSyntaxName() {
		if(xconn != null){
			return xconn.getClass().getSimpleName();
		}
		return undefined;
	}

	@Override
	public String getSpecificInfo() {
		if(xconn!=null && xconn.getSpecificCrossConnection()!=null)
			return xconn.getSpecificCrossConnection().toString();
		return undefined;
	}
	
	public String toString(){
		return getName();
	}

}
