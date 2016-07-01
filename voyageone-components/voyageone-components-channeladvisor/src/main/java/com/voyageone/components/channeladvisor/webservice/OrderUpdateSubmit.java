package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.bean.orders.BillingInfoUpdateSubmit;
import com.voyageone.components.channeladvisor.bean.orders.PaymentInfoUpdateSubmit;
import com.voyageone.components.channeladvisor.bean.orders.ShippingInfoUpdateSubmit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderUpdateSubmit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderUpdateSubmit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="NewClientOrderIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FlagStyle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FlagDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransactionNotes" type="{http://api.channeladvisor.com/webservices/}TransactionNoteSubmit" minOccurs="0"/>
 *         &lt;element name="OrderStatusUpdate" type="{http://api.channeladvisor.com/webservices/}OrderStatusUpdateSubmit" minOccurs="0"/>
 *         &lt;element name="BillingInfo" type="{http://api.channeladvisor.com/datacontracts/orders}BillingInfoUpdateSubmit" minOccurs="0"/>
 *         &lt;element name="ShippingInfo" type="{http://api.channeladvisor.com/datacontracts/orders}ShippingInfoUpdateSubmit" minOccurs="0"/>
 *         &lt;element name="PaymentInfo" type="{http://api.channeladvisor.com/datacontracts/orders}PaymentInfoUpdateSubmit" minOccurs="0"/>
 *         &lt;element name="RequestedShippingMethodInfo" type="{http://api.channeladvisor.com/webservices/}ShippingMethodInfoUpdateSubmit" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderUpdateSubmit", propOrder = { "orderID", "newClientOrderIdentifier", "flagStyle",
		"flagDescription", "transactionNotes", "orderStatusUpdate", "billingInfo", "shippingInfo", "paymentInfo",
		"requestedShippingMethodInfo" })
public class OrderUpdateSubmit {

	@XmlElement(name = "OrderID")
	protected int orderID;
	@XmlElement(name = "NewClientOrderIdentifier")
	protected String newClientOrderIdentifier;
	@XmlElement(name = "FlagStyle")
	protected String flagStyle;
	@XmlElement(name = "FlagDescription")
	protected String flagDescription;
	@XmlElement(name = "TransactionNotes")
	protected com.voyageone.components.channeladvisor.webservice.TransactionNoteSubmit transactionNotes;
	@XmlElement(name = "OrderStatusUpdate")
	protected com.voyageone.components.channeladvisor.webservice.OrderStatusUpdateSubmit orderStatusUpdate;
	@XmlElement(name = "BillingInfo")
	protected BillingInfoUpdateSubmit billingInfo;
	@XmlElement(name = "ShippingInfo")
	protected ShippingInfoUpdateSubmit shippingInfo;
	@XmlElement(name = "PaymentInfo")
	protected PaymentInfoUpdateSubmit paymentInfo;
	@XmlElement(name = "RequestedShippingMethodInfo")
	protected com.voyageone.components.channeladvisor.webservice.ShippingMethodInfoUpdateSubmit requestedShippingMethodInfo;

	/**
	 * Gets the value of the orderID property.
	 *
	 */
	public int getOrderID() {
		return orderID;
	}

	/**
	 * Sets the value of the orderID property.
	 *
	 */
	public void setOrderID(int value) {
		this.orderID = value;
	}

	/**
	 * Gets the value of the newClientOrderIdentifier property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getNewClientOrderIdentifier() {
		return newClientOrderIdentifier;
	}

	/**
	 * Sets the value of the newClientOrderIdentifier property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setNewClientOrderIdentifier(String value) {
		this.newClientOrderIdentifier = value;
	}

	/**
	 * Gets the value of the flagStyle property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getFlagStyle() {
		return flagStyle;
	}

	/**
	 * Sets the value of the flagStyle property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setFlagStyle(String value) {
		this.flagStyle = value;
	}

	/**
	 * Gets the value of the flagDescription property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getFlagDescription() {
		return flagDescription;
	}

	/**
	 * Sets the value of the flagDescription property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setFlagDescription(String value) {
		this.flagDescription = value;
	}

	/**
	 * Gets the value of the transactionNotes property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.TransactionNoteSubmit }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.TransactionNoteSubmit getTransactionNotes() {
		return transactionNotes;
	}

	/**
	 * Sets the value of the transactionNotes property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.TransactionNoteSubmit }
	 *
	 */
	public void setTransactionNotes(com.voyageone.components.channeladvisor.webservice.TransactionNoteSubmit value) {
		this.transactionNotes = value;
	}

	/**
	 * Gets the value of the orderStatusUpdate property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.OrderStatusUpdateSubmit }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.OrderStatusUpdateSubmit getOrderStatusUpdate() {
		return orderStatusUpdate;
	}

	/**
	 * Sets the value of the orderStatusUpdate property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.OrderStatusUpdateSubmit }
	 *
	 */
	public void setOrderStatusUpdate(com.voyageone.components.channeladvisor.webservice.OrderStatusUpdateSubmit value) {
		this.orderStatusUpdate = value;
	}

	/**
	 * Gets the value of the billingInfo property.
	 *
	 * @return possible object is {@link BillingInfoUpdateSubmit }
	 *
	 */
	public BillingInfoUpdateSubmit getBillingInfo() {
		return billingInfo;
	}

	/**
	 * Sets the value of the billingInfo property.
	 *
	 * @param value
	 *            allowed object is {@link BillingInfoUpdateSubmit }
	 *
	 */
	public void setBillingInfo(BillingInfoUpdateSubmit value) {
		this.billingInfo = value;
	}

	/**
	 * Gets the value of the shippingInfo property.
	 *
	 * @return possible object is {@link ShippingInfoUpdateSubmit }
	 *
	 */
	public ShippingInfoUpdateSubmit getShippingInfo() {
		return shippingInfo;
	}

	/**
	 * Sets the value of the shippingInfo property.
	 *
	 * @param value
	 *            allowed object is {@link ShippingInfoUpdateSubmit }
	 *
	 */
	public void setShippingInfo(ShippingInfoUpdateSubmit value) {
		this.shippingInfo = value;
	}

	/**
	 * Gets the value of the paymentInfo property.
	 *
	 * @return possible object is {@link PaymentInfoUpdateSubmit }
	 *
	 */
	public PaymentInfoUpdateSubmit getPaymentInfo() {
		return paymentInfo;
	}

	/**
	 * Sets the value of the paymentInfo property.
	 *
	 * @param value
	 *            allowed object is {@link PaymentInfoUpdateSubmit }
	 *
	 */
	public void setPaymentInfo(PaymentInfoUpdateSubmit value) {
		this.paymentInfo = value;
	}

	/**
	 * Gets the value of the requestedShippingMethodInfo property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.ShippingMethodInfoUpdateSubmit }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.ShippingMethodInfoUpdateSubmit getRequestedShippingMethodInfo() {
		return requestedShippingMethodInfo;
	}

	/**
	 * Sets the value of the requestedShippingMethodInfo property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.ShippingMethodInfoUpdateSubmit }
	 * 
	 */
	public void setRequestedShippingMethodInfo(com.voyageone.components.channeladvisor.webservice.ShippingMethodInfoUpdateSubmit value) {
		this.requestedShippingMethodInfo = value;
	}

}
