package com.voyageone.core.modelbean;

import java.io.Serializable;
import java.util.List;

public class PageNaviBean implements Serializable {

	private String permissionUrl;
	
	private List<NaviBean> navigationList;

	/**
	 * @return the permissionUrl
	 */
	public String getPermissionUrl() {
		return permissionUrl;
	}

	/**
	 * @param permissionUrl the permissionUrl to set
	 */
	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}

	/**
	 * @return the navigationList
	 */
	public List<NaviBean> getNavigationList() {
		return navigationList;
	}

	/**
	 * @param navigationList the navigationList to set
	 */
	public void setNavigationList(List<NaviBean> navigationList) {
		this.navigationList = navigationList;
	}

}
