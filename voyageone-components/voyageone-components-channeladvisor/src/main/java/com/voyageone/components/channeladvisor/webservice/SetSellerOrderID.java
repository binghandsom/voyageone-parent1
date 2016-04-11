package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.*;
import com.voyageone.components.channeladvisor.webservice.ArrayOfInt;

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
 *         &lt;element name="orderIDList" type="{http://api.channeladvisor.com/webservices/}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="sellerOrderIDList" type="{http://api.channeladvisor.com/webservices/}ArrayOfString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "accountID", "orderIDList", "sellerOrderIDList" })
@XmlRootElement(name = "SetSellerOrderID")
public class SetSellerOrderID {

	@XmlElement(required = true, nillable = true)
	protected String accountID;
	protected com.voyageone.components.channeladvisor.webservice.ArrayOfInt orderIDList;
	protected ArrayOfString sellerOrderIDList;

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
	 * Gets the value of the orderIDList property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfInt }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.ArrayOfInt getOrderIDList() {
		return orderIDList;
	}

	/**
	 * Sets the value of the orderIDList property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfInt }
	 *
	 */
	public void setOrderIDList(ArrayOfInt value) {
		this.orderIDList = value;
	}

	/**
	 * Gets the value of the sellerOrderIDList property.
	 * 
	 * @return possible object is {@link ArrayOfString }
	 * 
	 */
	public ArrayOfString getSellerOrderIDList() {
		return sellerOrderIDList;
	}

	/**
	 * Sets the value of the sellerOrderIDList property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfString }
	 * 
	 */
	public void setSellerOrderIDList(ArrayOfString value) {
		this.sellerOrderIDList = value;
	}

}
