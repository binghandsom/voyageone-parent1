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
 *         &lt;element name="SubmitOrderRefundResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfRefundOrderResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "submitOrderRefundResult" })
@XmlRootElement(name = "SubmitOrderRefundResponse")
public class SubmitOrderRefundResponse {

	@XmlElement(name = "SubmitOrderRefundResult")
	protected com.voyageone.components.channeladvisor.webservice.APIResultOfRefundOrderResponse submitOrderRefundResult;

	/**
	 * Gets the value of the submitOrderRefundResult property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.APIResultOfRefundOrderResponse }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.APIResultOfRefundOrderResponse getSubmitOrderRefundResult() {
		return submitOrderRefundResult;
	}

	/**
	 * Sets the value of the submitOrderRefundResult property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.APIResultOfRefundOrderResponse }
	 *
	 */
	public void setSubmitOrderRefundResult(APIResultOfRefundOrderResponse value) {
		this.submitOrderRefundResult = value;
	}

}
