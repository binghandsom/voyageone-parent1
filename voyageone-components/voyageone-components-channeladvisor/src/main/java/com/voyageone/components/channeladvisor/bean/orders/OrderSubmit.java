package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderSubmit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderSubmit">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}Order">
 *       &lt;sequence>
 *         &lt;element name="ShippingInfo" type="{http://api.channeladvisor.com/datacontracts/orders}ShippingInfoSubmit" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderSubmit", propOrder = { "shippingInfo" })
public class OrderSubmit extends Order {

	@XmlElement(name = "ShippingInfo")
	protected ShippingInfoSubmit shippingInfo;

	/**
	 * Gets the value of the shippingInfo property.
	 * 
	 * @return possible object is {@link ShippingInfoSubmit }
	 * 
	 */
	public ShippingInfoSubmit getShippingInfo() {
		return shippingInfo;
	}

	/**
	 * Sets the value of the shippingInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link ShippingInfoSubmit }
	 * 
	 */
	public void setShippingInfo(ShippingInfoSubmit value) {
		this.shippingInfo = value;
	}

}
