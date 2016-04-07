package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

/**
 * <p>
 * Java class for OrderResponseItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderResponseItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumberOfMatches" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OrderTimeGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="LastUpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="TotalOrderAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="OrderState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DateCancelledGMT" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ClientOrderIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SellerOrderID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FlagStyle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderResponseItem", propOrder = { "numberOfMatches", "orderTimeGMT", "lastUpdateDate",
		"totalOrderAmount", "orderState", "dateCancelledGMT", "orderID", "clientOrderIdentifier", "sellerOrderID",
		"flagStyle"/*, "shippingInfo", "orderStatus" */})
public abstract class OrderResponseItem {

	@XmlElement(name = "NumberOfMatches")
	protected int numberOfMatches;
	@XmlElement(name = "OrderTimeGMT", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar orderTimeGMT;
	@XmlElement(name = "LastUpdateDate", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar lastUpdateDate;
	@XmlElement(name = "TotalOrderAmount", required = true)
	protected BigDecimal totalOrderAmount;
	@XmlElement(name = "OrderState")
	protected String orderState;
	@XmlElement(name = "DateCancelledGMT", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar dateCancelledGMT;
	@XmlElement(name = "OrderID")
	protected int orderID;
	@XmlElement(name = "ClientOrderIdentifier")
	protected String clientOrderIdentifier;
	@XmlElement(name = "SellerOrderID")
	protected String sellerOrderID;
	@XmlElement(name = "FlagStyle")
	protected String flagStyle;
//	@XmlElement(name = "ShippingInfo")
//	protected ShippingInfoResponse shippingInfo;
//	@XmlElement(name = "OrderStatus")
//	protected OrderStatus orderStatus;
	/**
	 * Gets the value of the numberOfMatches property.
	 * 
	 */
	public int getNumberOfMatches() {
		return numberOfMatches;
	}

	/**
	 * Sets the value of the numberOfMatches property.
	 * 
	 */
	public void setNumberOfMatches(int value) {
		this.numberOfMatches = value;
	}

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
	 * Gets the value of the lastUpdateDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * Sets the value of the lastUpdateDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setLastUpdateDate(XMLGregorianCalendar value) {
		this.lastUpdateDate = value;
	}

	/**
	 * Gets the value of the totalOrderAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getTotalOrderAmount() {
		return totalOrderAmount;
	}

	/**
	 * Sets the value of the totalOrderAmount property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setTotalOrderAmount(BigDecimal value) {
		this.totalOrderAmount = value;
	}

	/**
	 * Gets the value of the orderState property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderState() {
		return orderState;
	}

	/**
	 * Sets the value of the orderState property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrderState(String value) {
		this.orderState = value;
	}

	/**
	 * Gets the value of the dateCancelledGMT property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDateCancelledGMT() {
		return dateCancelledGMT;
	}

	/**
	 * Sets the value of the dateCancelledGMT property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDateCancelledGMT(XMLGregorianCalendar value) {
		this.dateCancelledGMT = value;
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

//	public ShippingInfoResponse getShippingInfo() {
//		return shippingInfo;
//	}
//
//	public void setShippingInfo(ShippingInfoResponse shippingInfo) {
//		this.shippingInfo = shippingInfo;
//	}

//	public OrderStatus getOrderStatus() {
//		return orderStatus;
//	}
//
//	public void setOrderStatus(OrderStatus orderStatus) {
//		this.orderStatus = orderStatus;
//	}
}
