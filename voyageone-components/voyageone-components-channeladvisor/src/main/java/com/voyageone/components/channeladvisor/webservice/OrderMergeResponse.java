package com.voyageone.components.channeladvisor.webservice;

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
 *         &lt;element name="OrderMergeResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfBoolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "orderMergeResult" })
@XmlRootElement(name = "OrderMergeResponse")
public class OrderMergeResponse {

	@XmlElement(name = "OrderMergeResult")
	protected APIResultOfBoolean orderMergeResult;

	/**
	 * Gets the value of the orderMergeResult property.
	 * 
	 * @return possible object is {@link APIResultOfBoolean }
	 * 
	 */
	public APIResultOfBoolean getOrderMergeResult() {
		return orderMergeResult;
	}

	/**
	 * Sets the value of the orderMergeResult property.
	 * 
	 * @param value
	 *            allowed object is {@link APIResultOfBoolean }
	 * 
	 */
	public void setOrderMergeResult(APIResultOfBoolean value) {
		this.orderMergeResult = value;
	}

}
