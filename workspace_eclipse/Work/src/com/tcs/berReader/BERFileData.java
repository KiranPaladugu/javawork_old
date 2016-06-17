package com.tcs.berReader;

import com.marconi.fusion.X36.X36MsgGetReportBshrProtection;
import com.marconi.fusion.X36.X36MsgGetReportNodeConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportNodeCrossConnections;
import com.marconi.fusion.X36.X36MsgGetReportNodePossibleConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportProtectionConfiguration;
import com.marconi.fusion.X36.X36MsgGetReportProvisioning;
import com.marconi.fusion.X36.X36MsgReportGetCollection;
import com.marconi.fusion.X36.X36MsgReportGetDataProfiles;

public class BERFileData {

	public static final String[] members = { "X36.MsgGetReportNodeConfiguration", "X36.MsgGetReportNodePossibleConfiguration",
			"X36.MsgReportGetDataProfiles", "X36.MsgGetReportNodeCrossConnections", "X36.MsgGetReportProtectionConfiguration",
			"X36.MsgReportGetCollection", "X36.MsgGetReportBshrProtection", "X36.MsgGetReportProvisioning" };

	public static final String X36MsgGetReportNodeConfiguration = "X36.MsgGetReportNodeConfiguration";
	public static final String X36MsgReportGetDataProfiles = "X36.MsgReportGetDataProfiles";
	public static final String X36MsgGetReportNodeCrossConnections = "X36.MsgGetReportNodeCrossConnections";
	public static final String X36MsgGetReportProtectionConfiguration = "X36.MsgGetReportProtectionConfiguration";
	public static final String X36MsgReportGetCollection = "X36.MsgReportGetCollection";
	public static final String X36MsgGetReportBshrProtection = "X36.MsgGetReportBshrProtection";
	public static final String X36MsgGetReportProvisioning = "X36.MsgGetReportProvisioning";
	public static final String X36MsgGetReportNodePossibleConfiguration = "X36.MsgGetReportNodePossibleConfiguration";

	private X36MsgGetReportNodeConfiguration nodeConfig;
	private X36MsgReportGetDataProfiles dataProfiles;
	private X36MsgGetReportNodeCrossConnections nodeCrossConnections;
	private X36MsgGetReportProtectionConfiguration protectionConfig;
	private X36MsgReportGetCollection collection;
	private X36MsgGetReportBshrProtection bshrProtection;
	private X36MsgGetReportProvisioning provisioning;
	private X36MsgGetReportNodePossibleConfiguration nodePossibleConfig;

	public X36MsgReportGetDataProfiles getDataProfiles() {
		return dataProfiles;
	}

	public void setDataProfiles(final X36MsgReportGetDataProfiles dataProfiles) {
		this.dataProfiles = dataProfiles;
	}

	public X36MsgGetReportNodeCrossConnections getNodeCrossConnections() {
		return nodeCrossConnections;
	}

	public void setNodeCrossConnections(final X36MsgGetReportNodeCrossConnections nodeCrossConnections) {
		this.nodeCrossConnections = nodeCrossConnections;
	}

	public X36MsgGetReportProtectionConfiguration getProtectionConfig() {
		return protectionConfig;
	}

	public void setProtectionConfig(final X36MsgGetReportProtectionConfiguration protectionConfig) {
		this.protectionConfig = protectionConfig;
	}

	public X36MsgReportGetCollection getCollection() {
		return collection;
	}

	public void setCollection(final X36MsgReportGetCollection collection) {
		this.collection = collection;
	}

	public X36MsgGetReportBshrProtection getBshrProtection() {
		return bshrProtection;
	}

	public void setBshrProtection(final X36MsgGetReportBshrProtection bshrProtection) {
		this.bshrProtection = bshrProtection;
	}

	public X36MsgGetReportProvisioning getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(final X36MsgGetReportProvisioning provisioning) {
		this.provisioning = provisioning;
	}

	public X36MsgGetReportNodeConfiguration getNodeConfig() {
		return nodeConfig;
	}

	public void setNodeConfig(final X36MsgGetReportNodeConfiguration nodeConfig) {
		this.nodeConfig = nodeConfig;
	}

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder("");
		if (nodeConfig != null) {
			builder.append(nodeConfig.toString());
			builder.append("\n");
		}
		if (nodePossibleConfig != null) {
			builder.append(nodePossibleConfig.toString());
			builder.append("\n");
		}
		if (dataProfiles != null) {
			builder.append(dataProfiles.toString());
			builder.append("\n");
		}
		if (nodeCrossConnections != null) {
			builder.append(nodeCrossConnections.toString());
			builder.append("\n");
		}
		if (protectionConfig != null) {
			builder.append(protectionConfig.toString());
			builder.append("\n");
		}
		if (collection != null) {
			builder.append(collection.toString());
			builder.append("\n");
		}
		if (bshrProtection != null) {
			builder.append(bshrProtection.toString());
			builder.append("\n");
		}
		if (provisioning != null)
			builder.append(provisioning.toString());
		return builder.toString();
	}

	public int getMemberCount() {
		return members.length;
	}

	public String getMemberName(int index) {
		if (index > members.length) {
			return null;
		} else {
			return members[index];
		}
	}

	/**
	 * @return the nodePossibleConfig
	 */
	public X36MsgGetReportNodePossibleConfiguration getNodePossibleConfig() {
		return nodePossibleConfig;
	}

	/**
	 * @param nodePossibleConfig2
	 *            the nodePossibleConfig to set
	 */
	public void setNodePossibleConfig(X36MsgGetReportNodePossibleConfiguration nodePossibleConfig2) {
		this.nodePossibleConfig = nodePossibleConfig2;
	}

}
