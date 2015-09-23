package com.voyageone.cms.formbean;

import java.util.List;

public class FeedDefaultPropForm {
	
	private boolean isUpdate;
	
	private String channelId;
	
	private List<FeedDefaultPropBean> feedDefaultPropBeans;

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public List<FeedDefaultPropBean> getFeedDefaultPropBeans() {
		return feedDefaultPropBeans;
	}

	public void setFeedDefaultPropBeans(List<FeedDefaultPropBean> feedDefaultPropBeans) {
		this.feedDefaultPropBeans = feedDefaultPropBeans;
	}
	
}
