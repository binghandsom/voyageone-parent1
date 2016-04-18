package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.voyageone.components.channeladvisor.bean.orders.RefundOrderRequest;

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
 *         &lt;element name="request" type="{http://api.channeladvisor.com/datacontracts/orders}RefundOrderRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "accountID", "request" })
@XmlRootElement(name = "SubmitOrderRefund")
public class SubmitOrderRefund {

	@XmlElement(required = true, nillable = true)
	protected String accountID;
	@XmlElement(required = true, nillable = true)
	protected RefundOrderRequest request;

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
	 * Gets the value of the request property.
	 * 
	 * @return possible object is {@link RefundOrderRequest }
	 * 
	 */
	public RefundOrderRequest getRequest() {
		return request;
	}

	/**
	 * Sets the value of the request property.
	 * 
	 * @param value
	 *            allowed object is {@link RefundOrderRequest }
	 * 
	 */
	public void setRequest(RefundOrderRequest value) {
		this.request = value;
	}

}
