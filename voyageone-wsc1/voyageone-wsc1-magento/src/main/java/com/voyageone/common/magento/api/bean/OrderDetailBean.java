package com.voyageone.common.magento.api.bean;


public class OrderDetailBean {
	/**
	 * OMS订单号
	 */
	private String orderNumber;
	/**
	 * item_number
	 */
	private int itemNumber;
	/**
	 * adjustment
	 */
	private int adjustment;
	/**
	 * subItemNumber
	 */
	private int subItemNumber;
	/**
	 * sku
	 */
	private String sku;
	/**
	 * clientSku
	 */
	private String clientSku;
	/**
	 * 数量
	 */
	private int qty;
	/**
	 * 折扣
	 */
	private double discount;
	/**
	 * 物品原价
	 */
	private double price;
	/**
	 * 物品计算之后的推送价格
	 */
	private double realPrice;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public int getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(int adjustment) {
		this.adjustment = adjustment;
	}

	public int getSubItemNumber() {
		return subItemNumber;
	}

	public void setSubItemNumber(int subItemNumber) {
		this.subItemNumber = subItemNumber;
	}

	/**
	 * @return the sku
	 */
	public String getSku() {
		return sku;
	}
	/**
	 * @param sku the sku to set
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getClientSku() {
		return clientSku;
	}

	public void setClientSku(String clientSku) {
		this.clientSku = clientSku;
	}

	/**
	 * @return the qty
	 */
	public int getQty() {
		return qty;
	}
	/**
	 * @param qty the qty to set
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}
}
