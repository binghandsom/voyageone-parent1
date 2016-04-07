package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

/**
 * <p>
 * Java class for RefundOrderRequest complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RefundOrderRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClientOrderIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="AdjustmentReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SellerRefundID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RefundItems" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfRefundItem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefundOrderRequest", propOrder = { "clientOrderIdentifier", "orderID", "amount", "adjustmentReason",
		"sellerRefundID", "refundItems" })
public class RefundOrderRequest {

	@XmlElement(name = "ClientOrderIdentifier")
	protected String clientOrderIdentifier;
	@XmlElement(name = "OrderID")
	protected int orderID;
	@XmlElement(name = "Amount", required = true)
	protected BigDecimal amount;
	@XmlElement(name = "AdjustmentReason")
	protected String adjustmentReason;
	@XmlElement(name = "SellerRefundID")
	protected String sellerRefundID;
	@XmlElement(name = "RefundItems")
	protected ArrayOfRefundItem refundItems;

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
	 * Gets the value of the amount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Sets the value of the amount property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setAmount(BigDecimal value) {
		this.amount = value;
	}

	/**
	 * Gets the value of the adjustmentReason property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAdjustmentReason() {
		return adjustmentReason;
	}

	/**
	 * Sets the value of the adjustmentReason property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAdjustmentReason(String value) {
		this.adjustmentReason = value;
	}

	/**
	 * Gets the value of the sellerRefundID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSellerRefundID() {
		return sellerRefundID;
	}

	/**
	 * Sets the value of the sellerRefundID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSellerRefundID(String value) {
		this.sellerRefundID = value;
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

}
