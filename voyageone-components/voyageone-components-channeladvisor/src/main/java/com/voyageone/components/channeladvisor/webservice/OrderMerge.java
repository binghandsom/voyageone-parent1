package com.voyageone.components.channeladvisor.webservice;

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
 *         &lt;element name="primaryOrderID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="orderIDMergeList" type="{http://api.channeladvisor.com/webservices/}ArrayOfInt" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "accountID", "primaryOrderID", "orderIDMergeList" })
@XmlRootElement(name = "OrderMerge")
public class OrderMerge {

	@XmlElement(required = true, nillable = true)
	protected String accountID;
	protected int primaryOrderID;
	protected com.voyageone.components.channeladvisor.webservice.ArrayOfInt orderIDMergeList;

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
	 * Gets the value of the primaryOrderID property.
	 * 
	 */
	public int getPrimaryOrderID() {
		return primaryOrderID;
	}

	/**
	 * Sets the value of the primaryOrderID property.
	 * 
	 */
	public void setPrimaryOrderID(int value) {
		this.primaryOrderID = value;
	}

	/**
	 * Gets the value of the orderIDMergeList property.
	 * 
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfInt }
	 * 
	 */
	public com.voyageone.components.channeladvisor.webservice.ArrayOfInt getOrderIDMergeList() {
		return orderIDMergeList;
	}

	/**
	 * Sets the value of the orderIDMergeList property.
	 * 
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfInt }
	 * 
	 */
	public void setOrderIDMergeList(com.voyageone.components.channeladvisor.webservice.ArrayOfInt value) {
		this.orderIDMergeList = value;
	}

}
