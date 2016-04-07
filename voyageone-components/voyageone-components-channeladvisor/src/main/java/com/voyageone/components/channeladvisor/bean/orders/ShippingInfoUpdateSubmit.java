package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for ShippingInfoUpdateSubmit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ShippingInfoUpdateSubmit">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}ContactComplete">
 *       &lt;sequence>
 *         &lt;element name="EmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShippingInstructions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EstimatedShipDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="DeliveryDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShippingInfoUpdateSubmit", propOrder = { "emailAddress", "shippingInstructions", "estimatedShipDate",
		"deliveryDate" })
public class ShippingInfoUpdateSubmit extends ContactComplete {

	@XmlElement(name = "EmailAddress")
	protected String emailAddress;
	@XmlElement(name = "ShippingInstructions")
	protected String shippingInstructions;
	@XmlElement(name = "EstimatedShipDate", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar estimatedShipDate;
	@XmlElement(name = "DeliveryDate", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar deliveryDate;

	/**
	 * Gets the value of the emailAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the value of the emailAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEmailAddress(String value) {
		this.emailAddress = value;
	}

	/**
	 * Gets the value of the shippingInstructions property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShippingInstructions() {
		return shippingInstructions;
	}

	/**
	 * Sets the value of the shippingInstructions property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShippingInstructions(String value) {
		this.shippingInstructions = value;
	}

	/**
	 * Gets the value of the estimatedShipDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getEstimatedShipDate() {
		return estimatedShipDate;
	}

	/**
	 * Sets the value of the estimatedShipDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setEstimatedShipDate(XMLGregorianCalendar value) {
		this.estimatedShipDate = value;
	}

	/**
	 * Gets the value of the deliveryDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * Sets the value of the deliveryDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDeliveryDate(XMLGregorianCalendar value) {
		this.deliveryDate = value;
	}

}
