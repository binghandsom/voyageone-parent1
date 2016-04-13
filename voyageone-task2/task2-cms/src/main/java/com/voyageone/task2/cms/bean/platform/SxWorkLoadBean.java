package com.voyageone.task2.cms.bean.platform;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by zhujiaye on 16/2/14.
 */
public class SxWorkLoadBean extends BaseModel{
	private String channelId;
	private Long groupId;									        // 天猫用groupId进行上新
	private Long prodId;									        // 聚美用prodId进行上新
	private int promotionId;										// 活动id (目前只有聚美用到, 由cms进行管理)
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

	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public int getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(int publishStatus) {
		this.publishStatus = publishStatus;
	}
}
