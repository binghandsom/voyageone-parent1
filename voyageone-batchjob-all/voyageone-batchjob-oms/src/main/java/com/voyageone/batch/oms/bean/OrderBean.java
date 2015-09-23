package com.voyageone.batch.oms.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "order")
@XmlType(name = "order", propOrder = {"clientId","orderSource","orderNumber","orderDate","shipChoiceName","shippingAddress","orderItems","totals","payment"})
public class OrderBean {
	
	private int clientId=123;
	private String orderSource="TMALL";
	private String orderNumber;
	private String orderDate;
	private String shipChoiceName="Ship Outside System";
	private ShippingAddressBean shippingAddress=new ShippingAddressBean();
	
	@XmlElementWrapper(name = "orderItems")
	@XmlElement(name = "item")
	private List<OrderItemBean> orderItems;
	
	private TotalsBean totals;
	
	private PaymentBean payment = new PaymentBean();
	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}
	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	/**
	 * @return the orderSource
	 */
	public String getOrderSource() {
		return orderSource;
	}
	/**
	 * @param orderSource the orderSource to set
	 */
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the shipChoiceName
	 */
	public String getShipChoiceName() {
		return shipChoiceName;
	}
	/**
	 * @param shipChoiceName the shipChoiceName to set
	 */
	public void setShipChoiceName(String shipChoiceName) {
		this.shipChoiceName = shipChoiceName;
	}
	/**
	 * @return the shippingAddress
	 */
	public ShippingAddressBean getShippingAddress() {
		return shippingAddress;
	}
	/**
	 * @param shippingAddress the shippingAddress to set
	 */
	public void setShippingAddress(ShippingAddressBean shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	/**
	 * @return the orderItems
	 */
	public List<OrderItemBean> getOrderItems() {
		return orderItems;
	}
	/**
	 * @param orderItems the orderItems to set
	 */
	public void setOrderItems(List<OrderItemBean> orderItems) {
		this.orderItems = orderItems;
	}
	/**
	 * @return the totals
	 */
	public TotalsBean getTotals() {
		return totals;
	}
	/**
	 * @param totals the totals to set
	 */
	public void setTotals(TotalsBean totals) {
		this.totals = totals;
	}
	/**
	 * @return the payment
	 */
	public PaymentBean getPayment() {
		return payment;
	}
	/**
	 * @param payment the payment to set
	 */
	public void setPayment(PaymentBean payment) {
		this.payment = payment;
	}
	
	
}
