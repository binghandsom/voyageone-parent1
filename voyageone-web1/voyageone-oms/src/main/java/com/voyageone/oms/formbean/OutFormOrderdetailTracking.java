package com.voyageone.oms.formbean;

public class OutFormOrderdetailTracking {
	/**
	 * order_num
	 */
	private String orderNum;

	/**
	 * auto_number
	 */
	private String autoNumber;
	
	/**
	 * pickup_date
	 */
	private String pickupDate;
	
	/**
	 * cost
	 */
	private String cost;
	
	/**
	 * carrier
	 */
	private String carrier;
	
	/**
	 * delivery_confirmation
	 */
	private String deliveryConfirmation;
	
	/**
	 * tracking_id
	 */
	private String trackingId;
	
	/**
	 * freight_collect
	 */
	private String freightCollect;

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getAutoNumber() {
		return autoNumber;
	}

	public void setAutoNumber(String autoNumber) {
		this.autoNumber = autoNumber;
	}

	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getDeliveryConfirmation() {
		return deliveryConfirmation;
	}

	public void setDeliveryConfirmation(String deliveryConfirmation) {
		this.deliveryConfirmation = deliveryConfirmation;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getFreightCollect() {
		return freightCollect;
	}

	public void setFreightCollect(String freightCollect) {
		this.freightCollect = freightCollect;
	}
}
