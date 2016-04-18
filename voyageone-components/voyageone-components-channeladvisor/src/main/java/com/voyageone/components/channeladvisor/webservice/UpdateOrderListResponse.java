package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfOrderUpdateResponse;

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
 *         &lt;element name="UpdateOrderListResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfArrayOfOrderUpdateResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "updateOrderListResult" })
@XmlRootElement(name = "UpdateOrderListResponse")
public class UpdateOrderListResponse {

	@XmlElement(name = "UpdateOrderListResult")
	protected APIResultOfArrayOfOrderUpdateResponse updateOrderListResult;

	/**
	 * Gets the value of the updateOrderListResult property.
	 * 
	 * @return possible object is {@link APIResultOfArrayOfOrderUpdateResponse }
	 * 
	 */
	public APIResultOfArrayOfOrderUpdateResponse getUpdateOrderListResult() {
		return updateOrderListResult;
	}

	/**
	 * Sets the value of the updateOrderListResult property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link APIResultOfArrayOfOrderUpdateResponse }
	 * 
	 */
	public void setUpdateOrderListResult(APIResultOfArrayOfOrderUpdateResponse value) {
		this.updateOrderListResult = value;
	}

}
