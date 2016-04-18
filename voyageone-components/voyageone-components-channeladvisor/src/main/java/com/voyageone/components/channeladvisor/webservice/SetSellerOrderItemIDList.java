package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.ArrayOfInt;
import com.voyageone.components.channeladvisor.webservice.ArrayOfString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lineItemIDList" type="{http://api.channeladvisor.com/webservices/}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="sellerOrderItemIDList" type="{http://api.channeladvisor.com/webservices/}ArrayOfString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "accountID", "orderID", "lineItemIDList", "sellerOrderItemIDList" })
@XmlRootElement(name = "SetSellerOrderItemIDList")
public class SetSellerOrderItemIDList {

	@XmlElement(required = true, nillable = true)
	protected String accountID;
	protected int orderID;
	protected com.voyageone.components.channeladvisor.webservice.ArrayOfInt lineItemIDList;
	protected com.voyageone.components.channeladvisor.webservice.ArrayOfString sellerOrderItemIDList;

	/**
	 * Gets the value of the accountID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAccountID() {
		return accountID;
	}

	/**
	 * Sets the value of the accountID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAccountID(String value) {
		this.accountID = value;
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
	 * Gets the value of the lineItemIDList property.
	 * 
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfInt }
	 * 
	 */
	public com.voyageone.components.channeladvisor.webservice.ArrayOfInt getLineItemIDList() {
		return lineItemIDList;
	}

	/**
	 * Sets the value of the lineItemIDList property.
	 * 
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfInt }
	 * 
	 */
	public void setLineItemIDList(com.voyageone.components.channeladvisor.webservice.ArrayOfInt value) {
		this.lineItemIDList = value;
	}

	/**
	 * Gets the value of the sellerOrderItemIDList property.
	 * 
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfString }
	 * 
	 */
	public com.voyageone.components.channeladvisor.webservice.ArrayOfString getSellerOrderItemIDList() {
		return sellerOrderItemIDList;
	}

	/**
	 * Sets the value of the sellerOrderItemIDList property.
	 * 
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfString }
	 * 
	 */
	public void setSellerOrderItemIDList(com.voyageone.components.channeladvisor.webservice.ArrayOfString value) {
		this.sellerOrderItemIDList = value;
	}

}
