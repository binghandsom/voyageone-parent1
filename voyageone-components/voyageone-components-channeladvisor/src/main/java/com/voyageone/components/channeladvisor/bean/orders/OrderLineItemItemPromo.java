package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

/**
 * <p>
 * Java class for OrderLineItemItemPromo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderLineItemItemPromo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}OrderLineItemPromo">
 *       &lt;sequence>
 *         &lt;element name="ShippingPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderLineItemItemPromo", propOrder = { "shippingPrice" })
public class OrderLineItemItemPromo extends OrderLineItemPromo {

	@XmlElement(name = "ShippingPrice", required = true)
	protected BigDecimal shippingPrice;

	/**
	 * Gets the value of the shippingPrice property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getShippingPrice() {
		return shippingPrice;
	}

	/**
	 * Sets the value of the shippingPrice property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setShippingPrice(BigDecimal value) {
		this.shippingPrice = value;
	}

}
