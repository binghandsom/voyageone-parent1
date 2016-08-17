package com.voyageone.service.bean.com;

import com.voyageone.service.model.com.WmsMtStoreModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/12
 */
public class WmsMtStoreBean extends WmsMtStoreModel {
	
	private String channelName;
	
	private String mainStoreName;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getMainStoreName() {
		return mainStoreName;
	}

	public void setMainStoreName(String mainStoreName) {
		this.mainStoreName = mainStoreName;
	}
	
	
}
