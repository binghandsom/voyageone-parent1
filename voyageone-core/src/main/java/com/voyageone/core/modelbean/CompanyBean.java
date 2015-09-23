package com.voyageone.core.modelbean;

import java.util.List;

public class CompanyBean {

	private int companyId;

	private String companyName;

	private List<ChannelPermissionBean> channelList;
	/**
	 * @return the companyId
	 */
	public int getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the channelList
	 */
	public List<ChannelPermissionBean> getChannelList() {
		return channelList;
	}

	/**
	 * @param channelList the channelList to set
	 */
	public void setChannelList(List<ChannelPermissionBean> channelList) {
		this.channelList = channelList;
	}

}
