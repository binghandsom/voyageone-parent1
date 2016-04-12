package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.APIResultOfArrayOfInt32;

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
 *         &lt;element name="SetSellerOrderIDResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfArrayOfInt32" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "setSellerOrderIDResult" })
@XmlRootElement(name = "SetSellerOrderIDResponse")
public class SetSellerOrderIDResponse {

	@XmlElement(name = "SetSellerOrderIDResult")
	protected APIResultOfArrayOfInt32 setSellerOrderIDResult;

	/**
	 * Gets the value of the setSellerOrderIDResult property.
	 * 
	 * @return possible object is {@link APIResultOfArrayOfInt32 }
	 * 
	 */
	public APIResultOfArrayOfInt32 getSetSellerOrderIDResult() {
		return setSellerOrderIDResult;
	}

	/**
	 * Sets the value of the setSellerOrderIDResult property.
	 * 
	 * @param value
	 *            allowed object is {@link APIResultOfArrayOfInt32 }
	 * 
	 */
	public void setSetSellerOrderIDResult(APIResultOfArrayOfInt32 value) {
		this.setSellerOrderIDResult = value;
	}

}
