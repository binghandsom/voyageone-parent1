package com.voyageone.oms.formbean;

public class OutFormOrderdetailShipping {

	/**
	 * 订单号
	 */
	private String orderNumber;
	
	/**
	 * 发货日期
	 */
	private String dateShipped;
	
	/**
	 * res_id
	 */
	private String resId;
	
	/**
	 * sku
	 */
	private String sku;
	
	/**
	 * 快递公司
	 */
	private String trackingType;
	
	/**
	 * 快递单号
	 */
	private String trackingNo;

	/**
	 * 快递区域（1：国际，0：国内）
	 */
	private String trackingArea;	
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getDateShipped() {
		return dateShipped;
	}

	public void setDateShipped(String dateShipped) {
		this.dateShipped = dateShipped;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getTrackingType() {
		return trackingType;
	}

	public void setTrackingType(String trackingType) {
		this.trackingType = trackingType;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public String getTrackingArea() {
		return trackingArea;
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}
}
