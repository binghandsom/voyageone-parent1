package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.*;
import com.voyageone.components.channeladvisor.webservice.APIResultOfOrderRefundHistoryResponse;

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
 *         &lt;element name="GetOrderRefundHistoryResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfOrderRefundHistoryResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getOrderRefundHistoryResult" })
@XmlRootElement(name = "GetOrderRefundHistoryResponse")
public class GetOrderRefundHistoryResponse {

	@XmlElement(name = "GetOrderRefundHistoryResult")
	protected com.voyageone.components.channeladvisor.webservice.APIResultOfOrderRefundHistoryResponse getOrderRefundHistoryResult;

	/**
	 * Gets the value of the getOrderRefundHistoryResult property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.APIResultOfOrderRefundHistoryResponse }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.APIResultOfOrderRefundHistoryResponse getGetOrderRefundHistoryResult() {
		return getOrderRefundHistoryResult;
	}

	/**
	 * Sets the value of the getOrderRefundHistoryResult property.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link com.voyageone.components.channeladvisor.webservice.APIResultOfOrderRefundHistoryResponse }
	 *
	 */
	public void setGetOrderRefundHistoryResult(APIResultOfOrderRefundHistoryResponse value) {
		this.getOrderRefundHistoryResult = value;
	}

}
