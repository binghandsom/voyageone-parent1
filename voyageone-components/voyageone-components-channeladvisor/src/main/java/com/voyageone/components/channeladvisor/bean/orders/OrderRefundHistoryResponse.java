package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderRefundHistoryResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderRefundHistoryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ClientOrderIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RefundStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LineItemRefunds" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfOrderLineItemRefundHistoryResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderRefundHistoryResponse", propOrder = { "orderID", "clientOrderIdentifier", "refundStatus",
		"lineItemRefunds" })
public class OrderRefundHistoryResponse {

	@XmlElement(name = "OrderID")
	protected int orderID;
	@XmlElement(name = "ClientOrderIdentifier")
	protected String clientOrderIdentifier;
	@XmlElement(name = "RefundStatus")
	protected String refundStatus;
	@XmlElement(name = "LineItemRefunds")
	protected ArrayOfOrderLineItemRefundHistoryResponse lineItemRefunds;

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
	 * Gets the value of the refundStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRefundStatus() {
		return refundStatus;
	}

	/**
	 * Sets the value of the refundStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRefundStatus(String value) {
		this.refundStatus = value;
	}

	/**
	 * Gets the value of the lineItemRefunds property.
	 * 
	 * @return possible object is
	 *         {@link ArrayOfOrderLineItemRefundHistoryResponse }
	 * 
	 */
	public ArrayOfOrderLineItemRefundHistoryResponse getLineItemRefunds() {
		return lineItemRefunds;
	}

	/**
	 * Sets the value of the lineItemRefunds property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link ArrayOfOrderLineItemRefundHistoryResponse }
	 * 
	 */
	public void setLineItemRefunds(ArrayOfOrderLineItemRefundHistoryResponse value) {
		this.lineItemRefunds = value;
	}

}
