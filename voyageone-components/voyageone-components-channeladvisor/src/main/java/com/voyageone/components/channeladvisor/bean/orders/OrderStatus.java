package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for OrderStatus complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CheckoutStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CheckoutDateGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="PaymentStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaymentDateGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ShippingStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShippingDateGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="OrderRefundStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderStatus", propOrder = { "checkoutStatus", "checkoutDateGMT", "paymentStatus", "paymentDateGMT",
		"shippingStatus", "shippingDateGMT", "orderRefundStatus" })
public class OrderStatus {

	@XmlElement(name = "CheckoutStatus")
	protected String checkoutStatus;
	@XmlElement(name = "CheckoutDateGMT", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar checkoutDateGMT;
	@XmlElement(name = "PaymentStatus")
	protected String paymentStatus;
	@XmlElement(name = "PaymentDateGMT", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar paymentDateGMT;
	@XmlElement(name = "ShippingStatus")
	protected String shippingStatus;
	@XmlElement(name = "ShippingDateGMT", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar shippingDateGMT;
	@XmlElement(name = "OrderRefundStatus")
	protected String orderRefundStatus;

	/**
	 * Gets the value of the checkoutStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCheckoutStatus() {
		return checkoutStatus;
	}

	/**
	 * Sets the value of the checkoutStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCheckoutStatus(String value) {
		this.checkoutStatus = value;
	}

	/**
	 * Gets the value of the checkoutDateGMT property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getCheckoutDateGMT() {
		return checkoutDateGMT;
	}

	/**
	 * Sets the value of the checkoutDateGMT property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setCheckoutDateGMT(XMLGregorianCalendar value) {
		this.checkoutDateGMT = value;
	}

	/**
	 * Gets the value of the paymentStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * Sets the value of the paymentStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPaymentStatus(String value) {
		this.paymentStatus = value;
	}

	/**
	 * Gets the value of the paymentDateGMT property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getPaymentDateGMT() {
		return paymentDateGMT;
	}

	/**
	 * Sets the value of the paymentDateGMT property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setPaymentDateGMT(XMLGregorianCalendar value) {
		this.paymentDateGMT = value;
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

	/**
	 * Gets the value of the shippingDateGMT property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getShippingDateGMT() {
		return shippingDateGMT;
	}

	/**
	 * Sets the value of the shippingDateGMT property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setShippingDateGMT(XMLGregorianCalendar value) {
		this.shippingDateGMT = value;
	}

	/**
	 * Gets the value of the orderRefundStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderRefundStatus() {
		return orderRefundStatus;
	}

	/**
	 * Sets the value of the orderRefundStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrderRefundStatus(String value) {
		this.orderRefundStatus = value;
	}

}
