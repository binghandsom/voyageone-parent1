package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SetExportStatusResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="SetExportStatusResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ClientOrderIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetExportStatusResponse", propOrder = { "orderID", "clientOrderIdentifier", "success" })
public class SetExportStatusResponse {

	@XmlElement(name = "OrderID", required = true, type = Integer.class, nillable = true)
	protected Integer orderID;
	@XmlElement(name = "ClientOrderIdentifier")
	protected String clientOrderIdentifier;
	@XmlElement(name = "Success")
	protected boolean success;

	/**
	 * Gets the value of the orderID property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getOrderID() {
		return orderID;
	}

	/**
	 * Sets the value of the orderID property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setOrderID(Integer value) {
		this.orderID = value;
	}

	/**
	 * Gets the value of the clientOrderIdentifier property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClientOrderIdentifier() {
		return clientOrderIdentifier;
	}

	/**
	 * Sets the value of the clientOrderIdentifier property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClientOrderIdentifier(String value) {
		this.clientOrderIdentifier = value;
	}

	/**
	 * Gets the value of the success property.
	 * 
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Sets the value of the success property.
	 * 
	 */
	public void setSuccess(boolean value) {
		this.success = value;
	}

}
