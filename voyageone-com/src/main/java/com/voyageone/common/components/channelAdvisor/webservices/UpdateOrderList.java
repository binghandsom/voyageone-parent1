package com.voyageone.common.components.channelAdvisor.webservices;

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
 *         &lt;element name="updateOrderSubmitList" type="{http://api.channeladvisor.com/webservices/}ArrayOfOrderUpdateSubmit" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "accountID", "updateOrderSubmitList" })
@XmlRootElement(name = "UpdateOrderList")
public class UpdateOrderList {

	@XmlElement(required = true, nillable = true)
	protected String accountID;
	protected ArrayOfOrderUpdateSubmit updateOrderSubmitList;

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
	 * Gets the value of the updateOrderSubmitList property.
	 * 
	 * @return possible object is {@link ArrayOfOrderUpdateSubmit }
	 * 
	 */
	public ArrayOfOrderUpdateSubmit getUpdateOrderSubmitList() {
		return updateOrderSubmitList;
	}

	/**
	 * Sets the value of the updateOrderSubmitList property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfOrderUpdateSubmit }
	 * 
	 */
	public void setUpdateOrderSubmitList(ArrayOfOrderUpdateSubmit value) {
		this.updateOrderSubmitList = value;
	}

}
