package com.tcs.tree.view;

import com.marconi.fusion.X36.X36CardInformation;
import com.marconi.fusion.base.asn1.ASN1Exception;

public class TreeNodeCard extends AbstractSetTypeUserTreeNode {

	private X36CardInformation card;

	public TreeNodeCard(X36CardInformation card) {
		super(card);
		this.card = card;
	}

	@Override
	public String getSyntax() {
		if (card != null)
			return card.toString();
		else
			return "null";
	}

	@Override
	public String getNormalizedData() {
		try {
			return card.format();
		} catch (ASN1Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getName() {
		if (card != null && card.getCardId() != null)
			return "Card:" + card.getCardId().getValue();
		else
			return "<UN-DEFINED>";
	}

	@Override
	public int getId() {
		if (card != null && card.getCardId() != null)
			return card.getCardId().getValue();
		else
			return 0;
	}

	@Override
	public String getSyntaxName() {
		if (card != null)
			return card.getClass().getSimpleName();
		else
			return "<UNDEFINED>";
	}

	public String toString() {
		return getName();
	}

	@Override
	public String getSpecificInfo() {		
		if (card != null && card.getPhysicalCardInfo() != null)
			return card.getPhysicalCardInfo().toString();
		else
			return "<EMPTY>!";
	}
}
