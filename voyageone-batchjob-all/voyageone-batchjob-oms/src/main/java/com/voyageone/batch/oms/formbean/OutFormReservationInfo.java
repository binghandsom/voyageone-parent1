package com.voyageone.batch.oms.formbean;

public class OutFormReservationInfo {
	
	private String order_number;
	private String sourceorderid;
	private String sku;
	private String name;
	private String quantity;
	private String cart_id;
	private String orderStatus;
	private String item_number;
	
	public void setName(String name) {
		this.name = name;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setItem_number(String item_number) {
		this.item_number = item_number;
	}

	public String getName() {
		return name;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public String getItem_number() {
		return item_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	
	public void setSourceorderid(String sourceorderid) {
		this.sourceorderid = sourceorderid;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public void setCart_id(String cart_id) {
		this.cart_id = cart_id;
	}
	
	public String getOrder_number() {
		return order_number;
	}
	
	public String getSourceorderid() {
		return sourceorderid;
	}
	
	public String getSku() {
		return sku;
	}
	
	public String getQuantity() {
		return quantity;
	}
	
	public String getCart_id() {
		return cart_id;
	}
	
}
