package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.*;
import com.voyageone.components.channeladvisor.webservice.APIResultOfBoolean;

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
 *         &lt;element name="OrderSplitResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfBoolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "orderSplitResult" })
@XmlRootElement(name = "OrderSplitResponse")
public class OrderSplitResponse {

	@XmlElement(name = "OrderSplitResult")
	protected com.voyageone.components.channeladvisor.webservice.APIResultOfBoolean orderSplitResult;

	/**
	 * Gets the value of the orderSplitResult property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.APIResultOfBoolean }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.APIResultOfBoolean getOrderSplitResult() {
		return orderSplitResult;
	}

	/**
	 * Sets the value of the orderSplitResult property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.APIResultOfBoolean }
	 *
	 */
	public void setOrderSplitResult(APIResultOfBoolean value) {
		this.orderSplitResult = value;
	}

}
