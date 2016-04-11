package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.voyageone.components.channeladvisor.bean.orders.OrderCriteria;

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
 *         &lt;element name="orderCriteria" type="{http://api.channeladvisor.com/datacontracts/orders}OrderCriteria"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "accountID", "orderCriteria" })
@XmlRootElement(name = "GetOrderList")
public class GetOrderList {

	@XmlElement(required = true, nillable = true)
	protected String accountID;
	@XmlElement(required = true, nillable = true)
	protected OrderCriteria orderCriteria;

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
	 * Gets the value of the orderCriteria property.
	 * 
	 * @return possible object is {@link OrderCriteria }
	 * 
	 */
	public OrderCriteria getOrderCriteria() {
		return orderCriteria;
	}

	/**
	 * Sets the value of the orderCriteria property.
	 * 
	 * @param value
	 *            allowed object is {@link OrderCriteria }
	 * 
	 */
	public void setOrderCriteria(OrderCriteria value) {
		this.orderCriteria = value;
	}

}
