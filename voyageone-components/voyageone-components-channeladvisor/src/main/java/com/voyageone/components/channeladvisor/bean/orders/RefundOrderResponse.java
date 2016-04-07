package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for RefundOrderResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RefundOrderResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClientOrderIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RefundItems" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfRefundItem" minOccurs="0"/>
 *         &lt;element name="MessageCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefundOrderResponse", propOrder = { "clientOrderIdentifier", "orderID", "refundItems", "messageCode",
		"message" })
public class RefundOrderResponse {

	@XmlElement(name = "ClientOrderIdentifier")
	protected String clientOrderIdentifier;
	@XmlElement(name = "OrderID")
	protected int orderID;
	@XmlElement(name = "RefundItems")
	protected ArrayOfRefundItem refundItems;
	@XmlElement(name = "MessageCode")
	protected int messageCode;
	@XmlElement(name = "Message")
	protected String message;

	/**
	 * Gets the value of the clientOrderIdentifier property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClientOrderIdentifier() {
		return clientOrderIdentifier;
	}

	/**
	 * Sets the value of the clientOrderIdentifier property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClientOrderIdentifier(String value) {
		this.clientOrderIdentifier = value;
	}

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
	 * Gets the value of the refundItems property.
	 * 
	 * @return possible object is {@link ArrayOfRefundItem }
	 * 
	 */
	public ArrayOfRefundItem getRefundItems() {
		return refundItems;
	}

	/**
	 * Sets the value of the refundItems property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfRefundItem }
	 * 
	 */
	public void setRefundItems(ArrayOfRefundItem value) {
		this.refundItems = value;
	}

	/**
	 * Gets the value of the messageCode property.
	 * 
	 */
	public int getMessageCode() {
		return messageCode;
	}

	/**
	 * Sets the value of the messageCode property.
	 * 
	 */
	public void setMessageCode(int value) {
		this.messageCode = value;
	}

	/**
	 * Gets the value of the message property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the value of the message property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMessage(String value) {
		this.message = value;
	}

}
