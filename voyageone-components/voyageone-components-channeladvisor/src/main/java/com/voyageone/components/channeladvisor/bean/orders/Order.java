package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for Order complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Order">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderTimeGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ClientOrderIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SellerOrderID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OrderStatus" type="{http://api.channeladvisor.com/datacontracts/orders}OrderStatus" minOccurs="0"/>
 *         &lt;element name="BuyerEmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailOptIn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ResellerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BillingInfo" type="{http://api.channeladvisor.com/datacontracts/orders}BillingInfo" minOccurs="0"/>
 *         &lt;element name="PaymentInfo" type="{http://api.channeladvisor.com/datacontracts/orders}PaymentInfo" minOccurs="0"/>
 *         &lt;element name="ShoppingCart" type="{http://api.channeladvisor.com/datacontracts/orders}OrderCart" minOccurs="0"/>
 *         &lt;element name="CustomValueList" type="{http://api.channeladvisor.com/datacontracts/orders}ArrayOfCustomValue" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Order", propOrder = { "orderTimeGMT", "clientOrderIdentifier", "sellerOrderID", "orderStatus",
		"buyerEmailAddress", "emailOptIn", "resellerID", "billingInfo", "paymentInfo", "shoppingCart",
		"customValueList" })
public abstract class Order {

	@XmlElement(name = "OrderTimeGMT", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar orderTimeGMT;
	@XmlElement(name = "ClientOrderIdentifier")
	protected String clientOrderIdentifier;
	@XmlElement(name = "SellerOrderID")
	protected String sellerOrderID;
	@XmlElement(name = "OrderStatus")
	protected OrderStatus orderStatus;
	@XmlElement(name = "BuyerEmailAddress")
	protected String buyerEmailAddress;
	@XmlElement(name = "EmailOptIn")
	protected boolean emailOptIn;
	@XmlElement(name = "ResellerID")
	protected String resellerID;
	@XmlElement(name = "BillingInfo")
	protected BillingInfo billingInfo;
	@XmlElement(name = "PaymentInfo")
	protected PaymentInfo paymentInfo;
	@XmlElement(name = "ShoppingCart")
	protected OrderCart shoppingCart;
	@XmlElement(name = "CustomValueList")
	protected ArrayOfCustomValue customValueList;

	/**
	 * Gets the value of the orderTimeGMT property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getOrderTimeGMT() {
		return orderTimeGMT;
	}

	/**
	 * Sets the value of the orderTimeGMT property.
	 *
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setOrderTimeGMT(XMLGregorianCalendar value) {
		this.orderTimeGMT = value;
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
	 * Gets the value of the sellerOrderID property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getSellerOrderID() {
		return sellerOrderID;
	}

	/**
	 * Sets the value of the sellerOrderID property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setSellerOrderID(String value) {
		this.sellerOrderID = value;
	}

	/**
	 * Gets the value of the orderStatus property.
	 *
	 * @return possible object is {@link OrderStatus }
	 *
	 */
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Sets the value of the orderStatus property.
	 *
	 * @param value
	 *            allowed object is {@link OrderStatus }
	 *
	 */
	public void setOrderStatus(OrderStatus value) {
		this.orderStatus = value;
	}

	/**
	 * Gets the value of the buyerEmailAddress property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getBuyerEmailAddress() {
		return buyerEmailAddress;
	}

	/**
	 * Sets the value of the buyerEmailAddress property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setBuyerEmailAddress(String value) {
		this.buyerEmailAddress = value;
	}

	/**
	 * Gets the value of the emailOptIn property.
	 *
	 */
	public boolean isEmailOptIn() {
		return emailOptIn;
	}

	/**
	 * Sets the value of the emailOptIn property.
	 *
	 */
	public void setEmailOptIn(boolean value) {
		this.emailOptIn = value;
	}

	/**
	 * Gets the value of the resellerID property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getResellerID() {
		return resellerID;
	}

	/**
	 * Sets the value of the resellerID property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setResellerID(String value) {
		this.resellerID = value;
	}

	/**
	 * Gets the value of the billingInfo property.
	 *
	 * @return possible object is {@link BillingInfo }
	 *
	 */
	public BillingInfo getBillingInfo() {
		return billingInfo;
	}

	/**
	 * Sets the value of the billingInfo property.
	 *
	 * @param value
	 *            allowed object is {@link BillingInfo }
	 *
	 */
	public void setBillingInfo(BillingInfo value) {
		this.billingInfo = value;
	}

	/**
	 * Gets the value of the paymentInfo property.
	 *
	 * @return possible object is {@link PaymentInfo }
	 *
	 */
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	/**
	 * Sets the value of the paymentInfo property.
	 *
	 * @param value
	 *            allowed object is {@link PaymentInfo }
	 *
	 */
	public void setPaymentInfo(PaymentInfo value) {
		this.paymentInfo = value;
	}

	/**
	 * Gets the value of the shoppingCart property.
	 *
	 * @return possible object is {@link OrderCart }
	 *
	 */
	public OrderCart getShoppingCart() {
		return shoppingCart;
	}

	/**
	 * Sets the value of the shoppingCart property.
	 *
	 * @param value
	 *            allowed object is {@link OrderCart }
	 * 
	 */
	public void setShoppingCart(OrderCart value) {
		this.shoppingCart = value;
	}

	/**
	 * Gets the value of the customValueList property.
	 * 
	 * @return possible object is {@link ArrayOfCustomValue }
	 * 
	 */
	public ArrayOfCustomValue getCustomValueList() {
		return customValueList;
	}

	/**
	 * Sets the value of the customValueList property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfCustomValue }
	 * 
	 */
	public void setCustomValueList(ArrayOfCustomValue value) {
		this.customValueList = value;
	}

}
