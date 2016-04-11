package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Shipment complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Shipment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShippingCarrier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShippingClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TrackingNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Shipment", propOrder = { "shippingCarrier", "shippingClass", "trackingNumber" })
public class Shipment {

	@XmlElement(name = "ShippingCarrier")
	protected String shippingCarrier;
	@XmlElement(name = "ShippingClass")
	protected String shippingClass;
	@XmlElement(name = "TrackingNumber")
	protected String trackingNumber;

	/**
	 * Gets the value of the shippingCarrier property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShippingCarrier() {
		return shippingCarrier;
	}

	/**
	 * Sets the value of the shippingCarrier property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShippingCarrier(String value) {
		this.shippingCarrier = value;
	}

	/**
	 * Gets the value of the shippingClass property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShippingClass() {
		return shippingClass;
	}

	/**
	 * Sets the value of the shippingClass property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShippingClass(String value) {
		this.shippingClass = value;
	}

	/**
	 * Gets the value of the trackingNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTrackingNumber() {
		return trackingNumber;
	}

	/**
	 * Sets the value of the trackingNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTrackingNumber(String value) {
		this.trackingNumber = value;
	}

}
