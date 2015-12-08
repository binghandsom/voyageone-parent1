package com.voyageone.core.modelbean;

import java.io.Serializable;
import java.util.List;

public class ChannelPermissionBean implements Serializable {

	
	private String channelId;
	private String channelName;
	private String channelImgUrl;
	private List<String> menuList;
	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}
	/**
	 * @param channelName the channelName to set
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	/**
	 * @return the channelImgUrl
	 */
	public String getChannelImgUrl() {
		return channelImgUrl;
	}
	/**
	 * @param channelImgUrl the channelImgUrl to set
	 */
	public void setChannelImgUrl(String channelImgUrl) {
		this.channelImgUrl = channelImgUrl;
	}
	/**
	 * @return the menuList
	 */
	public List<String> getMenuList() {
		return menuList;
	}
	/**
	 * @param menuList the menuList to set
	 */
	public void setMenuList(List<String> menuList) {
		this.menuList = menuList;
	}
	
	
}
