package com.tcs.berReader.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.framework.reg.Register;
import com.marconi.fusion.X36.X36CardInformation;
import com.marconi.fusion.X36.X36GetReportNodeConfiguration;
import com.marconi.fusion.X36.X36NetworkElement;
import com.marconi.fusion.X36.X36PortInformation;
import com.marconi.fusion.X36.X36SetOfCardInformation;
import com.marconi.fusion.X36.X36SetOfPortInformation;
import com.marconi.fusion.X36.X36SetOfShelfInformation;
import com.marconi.fusion.X36.X36ShelfInformation;
import com.tcs.berReader.BERFileData;

public class NodeConfigQueryHandler {
	private BERFileData data;
	private X36GetReportNodeConfiguration nodeConfig;
	private X36SetOfShelfInformation shelfs;
	private Map<LocationInfo, X36SetOfCardInformation> cardsInfo = new HashMap<LocationInfo, X36SetOfCardInformation>();
	private Map<LocationInfo, X36SetOfPortInformation> portsInfo = new HashMap<LocationInfo, X36SetOfPortInformation>();
	private X36NetworkElement nodeDetails;

	public BERFileData getData() {
		return data;
	}

	private void setData(BERFileData data) {
		if (data != null) {
			this.data = data;
			if (data.getNodeConfig() != null) {
				nodeConfig = this.data.getNodeConfig().getBody();
			}
		}
		if (nodeConfig != null)
			processData();
	}

	private void processData() {
		shelfs = nodeConfig.getShelves();
		nodeDetails = nodeConfig.getNetworkElement();
		for (X36ShelfInformation shelf : shelfs) {
			X36SetOfCardInformation cards = shelf.getCards();
			LocationInfo info = new LocationInfo();
			info.setShelf(shelf.getShelfId().getValue());
			cardsInfo.put(info, cards);
			for (X36CardInformation card : cards) {
				LocationInfo li = new LocationInfo();
				li.setShelf(shelf.getShelfId().getValue());
				li.setCard(card.getCardId().getValue());
				X36SetOfPortInformation ports = card.getPorts();
				portsInfo.put(li, ports);
			}
		}

	}

	public NodeConfigQueryHandler(BERFileData data) {
		Register.register(this);
		setData(data);
	}

	public X36GetReportNodeConfiguration getNodeConfiguraion() {
		return nodeConfig;
	}

	public X36SetOfShelfInformation getShelfInfo() {
		return shelfs;
	}

	public X36NetworkElement getNetwokDetails() {
		return nodeDetails;
	}

	public X36ShelfInformation getShelfInformation(int shelfId) {
		for (X36ShelfInformation shelf : shelfs) {
			if (shelf.getShelfId().getValue() == shelfId) {
				return shelf;
			}
		}
		// MessageHandler.displayErrorMessage("Shelf with ID < "+shelfId+" > Not found!");
		return null;
	}

	public X36SetOfCardInformation getCards(int shelfId) {
		return cardsInfo.get(shelfId);
	}

	public X36SetOfCardInformation getCard(LocationInfo location) {
		return cardsInfo.get(location);
	}

	public X36SetOfPortInformation getPorts(LocationInfo locationInfo) {
		return getPortInformation(locationInfo);
	}

	public X36PortInformation getPortInfo(LocationInfo locationInfo) {
		try {
			X36SetOfPortInformation ports = getPortInformation(locationInfo);
			for (X36PortInformation port : ports) {
				if (port.getPortId().getValue() == locationInfo.getPort()) {
					return port;
				}
			}
		} catch (Exception e) {
			// MessageHandler.displayErrorMessage("UnIdentified Inputs.");
		}
		return null;
	}

	private X36SetOfPortInformation getPortInformation(LocationInfo locationInfo) {
		LocationInfo loc = new LocationInfo();
		loc.setShelf(locationInfo.getShelf());
		loc.setCard(locationInfo.getCard());
		Set<LocationInfo> keys = portsInfo.keySet();
		Iterator<LocationInfo> itr = keys.iterator();
		while (itr.hasNext()) {
			LocationInfo obj = itr.next();
			if (obj.equals(loc)) {
				return portsInfo.get(obj);
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private X36SetOfCardInformation getCardsInfo(LocationInfo locationInfo) {
		LocationInfo loc = new LocationInfo();
		loc.setShelf(locationInfo.getShelf());
		Set<LocationInfo> keys = cardsInfo.keySet();
		Iterator<LocationInfo> itr = keys.iterator();
		while (itr.hasNext()) {
			LocationInfo obj = itr.next();
			if (obj.equals(loc)) {
				return cardsInfo.get(obj);
			}
		}
		return null;
	}
}
