package com.voyageone.components.channeladvisor.webservice;

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
 *         &lt;element name="SubmitOrderResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfInt32" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "submitOrderResult" })
@XmlRootElement(name = "SubmitOrderResponse")
public class SubmitOrderResponse {

	@XmlElement(name = "SubmitOrderResult")
	protected com.voyageone.components.channeladvisor.webservice.APIResultOfInt32 submitOrderResult;

	/**
	 * Gets the value of the submitOrderResult property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.APIResultOfInt32 }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.APIResultOfInt32 getSubmitOrderResult() {
		return submitOrderResult;
	}

	/**
	 * Sets the value of the submitOrderResult property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.APIResultOfInt32 }
	 *
	 */
	public void setSubmitOrderResult(APIResultOfInt32 value) {
		this.submitOrderResult = value;
	}

}
