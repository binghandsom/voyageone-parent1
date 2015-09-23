package com.voyageone.batch.oms.modelbean;

public class CustomerBean {
	/**
	 * 顾客ID
	 */
	private String customerId;
	/**
	 * 平台ID
	 */
	private String platformId;
	/**
	 * 店铺渠道
	 */
	private String orderChannelId;
	/**
	 * 昵称（注册名）
	 */
	private String lastName;
	
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the platformId
	 */
	public String getPlatformId() {
		return platformId;
	}
	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	/**
	 * @return the orderChannelId
	 */
	public String getOrderChannelId() {
		return orderChannelId;
	}
	/**
	 * @param orderChannelId the orderChannelId to set
	 */
	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}

