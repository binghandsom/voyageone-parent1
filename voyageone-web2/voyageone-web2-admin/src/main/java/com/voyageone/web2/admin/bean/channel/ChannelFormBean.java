package com.voyageone.web2.admin.bean.channel;

import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/10
 */
public class ChannelFormBean extends AdminFormBean {

	private Integer companyId;
	
	private String orderChannelId;
	
	private String name;
	
	private String fullName;
	
	private String channelName;
	
	private String imgUrl;
	
	private String sendName;
	
	private String sendAddress;
	
	private String sendTel;
	
	private String sendZip;
	
	private String screctKey;
	
	private String sessionKey;
	
	private Integer isUsjoi;
	
	private String cartIds;
	
	private Integer active;

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSendTel() {
		return sendTel;
	}

	public void setSendTel(String sendTel) {
		this.sendTel = sendTel;
	}

	public String getSendZip() {
		return sendZip;
	}

	public void setSendZip(String sendZip) {
		this.sendZip = sendZip;
	}

	public String getScrectKey() {
		return screctKey;
	}

	public void setScrectKey(String screctKey) {
		this.screctKey = screctKey;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Integer getIsUsjoi() {
		return isUsjoi;
	}

	public void setIsUsjoi(Integer isUsjoi) {
		this.isUsjoi = isUsjoi;
	}

	public String getCartIds() {
		return cartIds;
	}

	public void setCartIds(String cartIds) {
		this.cartIds = cartIds;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}
	
}
