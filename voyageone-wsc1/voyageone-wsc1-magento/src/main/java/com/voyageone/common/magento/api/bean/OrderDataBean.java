package com.voyageone.common.magento.api.bean;

import java.util.ArrayList;
import java.util.List;

public class OrderDataBean {
	private String orderNumber;
	private String billingName;
	private String billingState;
	private String billingCity;
	private String billingCountry;
	private String billingAddress;
	private String billingAddress2;
	private String billingPostcode;
	private String billingTelephone;
	private String billingEmail;
	private String shippingName;
	private String shippingEmail;
	private String shippingState;
	private String shippingCity;
	private String shippingCountry;
	private String shippingAddress;
	private String shippingAddress2;
	private String shippingPostcode;
	private String origin_source_order_id;
	private String pay_no;
	private String taobao_logistics_id;
	private String shippingTelephone;
	private String shippingMethod;
	private String paymentMethod;
	private List<OrderDetailBean> orderDetails;
	private double discount = 0;
	private double surcharge = 0;
	private double finalGrandTotal = 0;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the billingName
	 */
	public String getBillingName() {
		return billingName;
	}
	/**
	 * @param billingName the billingName to set
	 */
	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}
	/**
	 * @return the billingState
	 */
	public String getBillingState() {
		return billingState;
	}
	/**
	 * @param billingState the billingState to set
	 */
	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}
	/**
	 * @return the billingCity
	 */
	public String getBillingCity() {
		return billingCity;
	}
	/**
	 * @param billingCity the billingCity to set
	 */
	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}
	/**
	 * @return the billingCountry
	 */
	public String getBillingCountry() {
		return billingCountry;
	}
	/**
	 * @param billingCountry the billingCountry to set
	 */
	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}
	/**
	 * @return the billingAddress
	 */
	public String getBillingAddress() {
		return billingAddress;
	}
	/**
	 * @param billingAddress the billingAddress to set
	 */
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	/**
	 * @return the billingPostcode
	 */
	public String getBillingPostcode() {
		return billingPostcode;
	}
	/**
	 * @param billingPostcode the billingPostcode to set
	 */
	public void setBillingPostcode(String billingPostcode) {
		this.billingPostcode = billingPostcode;
	}
	/**
	 * @return the billingTelephone
	 */
	public String getBillingTelephone() {
		return billingTelephone;
	}
	/**
	 * @param billingTelephone the billingTelephone to set
	 */
	public void setBillingTelephone(String billingTelephone) {
		this.billingTelephone = billingTelephone;
	}

	public String getBillingEmail() {
		return billingEmail;
	}

	public void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail;
	}

	public String getShippingEmail() {
		return shippingEmail;
	}

	public void setShippingEmail(String shippingEmail) {
		this.shippingEmail = shippingEmail;
	}

	/**
	 * @return the shippingName
	 */
	public String getShippingName() {
		return shippingName;
	}
	/**
	 * @param shippingName the shippingName to set
	 */
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	/**
	 * @return the shippingState
	 */
	public String getShippingState() {
		return shippingState;
	}
	/**
	 * @param shippingState the shippingState to set
	 */
	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}
	/**
	 * @return the shippingCity
	 */
	public String getShippingCity() {
		return shippingCity;
	}
	/**
	 * @param shippingCity the shippingCity to set
	 */
	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}
	/**
	 * @return the shippingCountry
	 */
	public String getShippingCountry() {
		return shippingCountry;
	}
	/**
	 * @param shippingCountry the shippingCountry to set
	 */
	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}
	/**
	 * @return the shippingAddress
	 */
	public String getShippingAddress() {
		return shippingAddress;
	}
	/**
	 * @param shippingAddress the shippingAddress to set
	 */
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	/**
	 * @return the shippingPostcode
	 */
	public String getShippingPostcode() {
		return shippingPostcode;
	}
	/**
	 * @param shippingPostcode the shippingPostcode to set
	 */
	public void setShippingPostcode(String shippingPostcode) {
		this.shippingPostcode = shippingPostcode;
	}
	/**
	 * @return the shippingTelephone
	 */
	public String getShippingTelephone() {
		return shippingTelephone;
	}
	/**
	 * @param shippingTelephone the shippingTelephone to set
	 */
	public void setShippingTelephone(String shippingTelephone) {
		this.shippingTelephone = shippingTelephone;
	}
	/**
	 * @return the shippingMethod
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}
	/**
	 * @param shippingMethod the shippingMethod to set
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	/**
	 * @return the orderDetails
	 */
	public List<OrderDetailBean> getOrderDetails() {
		return orderDetails;
	}
	/**
	 * @param orderDetails the orderDetails to set
	 */
	public void setOrderDetails(List<OrderDetailBean> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public double getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getShippingAddress2() {
		return shippingAddress2;
	}

	public void setShippingAddress2(String shippingAddress2) {
		this.shippingAddress2 = shippingAddress2;
	}

	public double getFinalGrandTotal() {
		return finalGrandTotal;
	}

	public void setFinalGrandTotal(double finalGrandTotal) {
		this.finalGrandTotal = finalGrandTotal;
	}

	public String getOrigin_source_order_id() {
		return origin_source_order_id;
	}

	public void setOrigin_source_order_id(String origin_source_order_id) {
		this.origin_source_order_id = origin_source_order_id;
	}

	public String getPay_no() {
		return pay_no;
	}

	public void setPay_no(String pay_no) {
		this.pay_no = pay_no;
	}

	public String getTaobao_logistics_id() {
		return taobao_logistics_id;
	}

	public void setTaobao_logistics_id(String taobao_logistics_id) {
		this.taobao_logistics_id = taobao_logistics_id;
	}
}
