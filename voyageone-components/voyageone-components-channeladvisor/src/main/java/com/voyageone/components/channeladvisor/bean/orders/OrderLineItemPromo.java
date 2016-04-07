package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderLineItemPromo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderLineItemPromo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}OrderLineItemBase">
 *       &lt;sequence>
 *         &lt;element name="PromoCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderLineItemPromo", propOrder = { "promoCode" })
public class OrderLineItemPromo extends OrderLineItemBase {

	@XmlElement(name = "PromoCode")
	protected String promoCode;

	/**
	 * Gets the value of the promoCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPromoCode() {
		return promoCode;
	}

	/**
	 * Sets the value of the promoCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPromoCode(String value) {
		this.promoCode = value;
	}

}
