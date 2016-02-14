package com.voyageone.batch.cms.bean.platform;

/**
 * Created by zhujiaye on 16/2/14.
 */
public class SxWorkLoadBean {
	private String channelId;
	private Long groupId;
	private int publishStatus;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public int getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(int publishStatus) {
		this.publishStatus = publishStatus;
	}
}
