package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfBoolean;

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
 *         &lt;element name="SetSellerOrderItemIDListResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfArrayOfBoolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "setSellerOrderItemIDListResult" })
@XmlRootElement(name = "SetSellerOrderItemIDListResponse")
public class SetSellerOrderItemIDListResponse {

	@XmlElement(name = "SetSellerOrderItemIDListResult")
	protected APIResultOfArrayOfBoolean setSellerOrderItemIDListResult;

	/**
	 * Gets the value of the setSellerOrderItemIDListResult property.
	 * 
	 * @return possible object is {@link APIResultOfArrayOfBoolean }
	 * 
	 */
	public APIResultOfArrayOfBoolean getSetSellerOrderItemIDListResult() {
		return setSellerOrderItemIDListResult;
	}

	/**
	 * Sets the value of the setSellerOrderItemIDListResult property.
	 * 
	 * @param value
	 *            allowed object is {@link APIResultOfArrayOfBoolean }
	 * 
	 */
	public void setSetSellerOrderItemIDListResult(APIResultOfArrayOfBoolean value) {
		this.setSellerOrderItemIDListResult = value;
	}

}
