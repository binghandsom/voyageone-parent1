package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderStatusUpdateSubmit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderStatusUpdateSubmit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CheckoutPaymentStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShippingStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderStatusUpdateSubmit", propOrder = { "checkoutPaymentStatus", "shippingStatus" })
public class OrderStatusUpdateSubmit {

	@XmlElement(name = "CheckoutPaymentStatus")
	protected String checkoutPaymentStatus;
	@XmlElement(name = "ShippingStatus")
	protected String shippingStatus;

	/**
	 * Gets the value of the checkoutPaymentStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCheckoutPaymentStatus() {
		return checkoutPaymentStatus;
	}

	/**
	 * Sets the value of the checkoutPaymentStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCheckoutPaymentStatus(String value) {
		this.checkoutPaymentStatus = value;
	}

	/**
	 * Gets the value of the shippingStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShippingStatus() {
		return shippingStatus;
	}

	/**
	 * Sets the value of the shippingStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShippingStatus(String value) {
		this.shippingStatus = value;
	}

}
