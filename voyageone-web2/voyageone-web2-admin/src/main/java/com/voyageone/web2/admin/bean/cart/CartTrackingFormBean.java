package com.voyageone.web2.admin.bean.cart;

import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/22
 */
public class CartTrackingFormBean extends AdminFormBean {
	
	private Integer seq;
	
	private String orderChannelId;
	
	private Integer cartId;
	
	private String trackingStatus;
	
	private String trackingInfo;
	
	private String location;
	
	private String displayFlg;
	
	private String displayStatus;
	
	private String trackingArea;
	
	private String trackingSpreadFlg;
	
	private String comment;
	
	private Boolean active;

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public String getTrackingStatus() {
		return trackingStatus;
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

	public String getTrackingInfo() {
		return trackingInfo;
	}

	public void setTrackingInfo(String trackingInfo) {
		this.trackingInfo = trackingInfo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDisplayFlg() {
		return displayFlg;
	}

	public void setDisplayFlg(String displayFlg) {
		this.displayFlg = displayFlg;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public String getTrackingArea() {
		return trackingArea;
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getTrackingSpreadFlg() {
		return trackingSpreadFlg;
	}

	public void setTrackingSpreadFlg(String trackingSpreadFlg) {
		this.trackingSpreadFlg = trackingSpreadFlg;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
