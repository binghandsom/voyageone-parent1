package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderUpdateResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderUpdateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FlagAndNotesSuccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="FlagAndNotesMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderStatusSuccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OrderStatusMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShippingAndCOIDSuccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ShippingAndCOIDMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BillingAndPaymentSuccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="BillingAndPaymentMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestedShippingMethodSuccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RequestedShippingMethodMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderUpdateResponse", propOrder = { "flagAndNotesSuccess", "flagAndNotesMessage",
		"orderStatusSuccess", "orderStatusMessage", "shippingAndCOIDSuccess", "shippingAndCOIDMessage",
		"billingAndPaymentSuccess", "billingAndPaymentMessage", "requestedShippingMethodSuccess",
		"requestedShippingMethodMessage" })
public class OrderUpdateResponse {

	@XmlElement(name = "FlagAndNotesSuccess")
	protected boolean flagAndNotesSuccess;
	@XmlElement(name = "FlagAndNotesMessage")
	protected String flagAndNotesMessage;
	@XmlElement(name = "OrderStatusSuccess")
	protected boolean orderStatusSuccess;
	@XmlElement(name = "OrderStatusMessage")
	protected String orderStatusMessage;
	@XmlElement(name = "ShippingAndCOIDSuccess")
	protected boolean shippingAndCOIDSuccess;
	@XmlElement(name = "ShippingAndCOIDMessage")
	protected String shippingAndCOIDMessage;
	@XmlElement(name = "BillingAndPaymentSuccess")
	protected boolean billingAndPaymentSuccess;
	@XmlElement(name = "BillingAndPaymentMessage")
	protected String billingAndPaymentMessage;
	@XmlElement(name = "RequestedShippingMethodSuccess")
	protected boolean requestedShippingMethodSuccess;
	@XmlElement(name = "RequestedShippingMethodMessage")
	protected String requestedShippingMethodMessage;

	/**
	 * Gets the value of the flagAndNotesSuccess property.
	 * 
	 */
	public boolean isFlagAndNotesSuccess() {
		return flagAndNotesSuccess;
	}

	/**
	 * Sets the value of the flagAndNotesSuccess property.
	 * 
	 */
	public void setFlagAndNotesSuccess(boolean value) {
		this.flagAndNotesSuccess = value;
	}

	/**
	 * Gets the value of the flagAndNotesMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlagAndNotesMessage() {
		return flagAndNotesMessage;
	}

	/**
	 * Sets the value of the flagAndNotesMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFlagAndNotesMessage(String value) {
		this.flagAndNotesMessage = value;
	}

	/**
	 * Gets the value of the orderStatusSuccess property.
	 * 
	 */
	public boolean isOrderStatusSuccess() {
		return orderStatusSuccess;
	}

	/**
	 * Sets the value of the orderStatusSuccess property.
	 * 
	 */
	public void setOrderStatusSuccess(boolean value) {
		this.orderStatusSuccess = value;
	}

	/**
	 * Gets the value of the orderStatusMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderStatusMessage() {
		return orderStatusMessage;
	}

	/**
	 * Sets the value of the orderStatusMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrderStatusMessage(String value) {
		this.orderStatusMessage = value;
	}

	/**
	 * Gets the value of the shippingAndCOIDSuccess property.
	 * 
	 */
	public boolean isShippingAndCOIDSuccess() {
		return shippingAndCOIDSuccess;
	}

	/**
	 * Sets the value of the shippingAndCOIDSuccess property.
	 * 
	 */
	public void setShippingAndCOIDSuccess(boolean value) {
		this.shippingAndCOIDSuccess = value;
	}

	/**
	 * Gets the value of the shippingAndCOIDMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShippingAndCOIDMessage() {
		return shippingAndCOIDMessage;
	}

	/**
	 * Sets the value of the shippingAndCOIDMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShippingAndCOIDMessage(String value) {
		this.shippingAndCOIDMessage = value;
	}

	/**
	 * Gets the value of the billingAndPaymentSuccess property.
	 * 
	 */
	public boolean isBillingAndPaymentSuccess() {
		return billingAndPaymentSuccess;
	}

	/**
	 * Sets the value of the billingAndPaymentSuccess property.
	 * 
	 */
	public void setBillingAndPaymentSuccess(boolean value) {
		this.billingAndPaymentSuccess = value;
	}

	/**
	 * Gets the value of the billingAndPaymentMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBillingAndPaymentMessage() {
		return billingAndPaymentMessage;
	}

	/**
	 * Sets the value of the billingAndPaymentMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBillingAndPaymentMessage(String value) {
		this.billingAndPaymentMessage = value;
	}

	/**
	 * Gets the value of the requestedShippingMethodSuccess property.
	 * 
	 */
	public boolean isRequestedShippingMethodSuccess() {
		return requestedShippingMethodSuccess;
	}

	/**
	 * Sets the value of the requestedShippingMethodSuccess property.
	 * 
	 */
	public void setRequestedShippingMethodSuccess(boolean value) {
		this.requestedShippingMethodSuccess = value;
	}

	/**
	 * Gets the value of the requestedShippingMethodMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRequestedShippingMethodMessage() {
		return requestedShippingMethodMessage;
	}

	/**
	 * Sets the value of the requestedShippingMethodMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRequestedShippingMethodMessage(String value) {
		this.requestedShippingMethodMessage = value;
	}

}
