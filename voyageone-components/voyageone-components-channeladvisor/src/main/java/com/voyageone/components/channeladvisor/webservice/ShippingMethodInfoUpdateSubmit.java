package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ShippingMethodInfoUpdateSubmit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ShippingMethodInfoUpdateSubmit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CarrierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClassCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShippingMethodInfoUpdateSubmit", propOrder = { "carrierCode", "classCode" })
public class ShippingMethodInfoUpdateSubmit {

	@XmlElement(name = "CarrierCode")
	protected String carrierCode;
	@XmlElement(name = "ClassCode")
	protected String classCode;

	/**
	 * Gets the value of the carrierCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCarrierCode() {
		return carrierCode;
	}

	/**
	 * Sets the value of the carrierCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCarrierCode(String value) {
		this.carrierCode = value;
	}

	/**
	 * Gets the value of the classCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClassCode() {
		return classCode;
	}

	/**
	 * Sets the value of the classCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClassCode(String value) {
		this.classCode = value;
	}

}
