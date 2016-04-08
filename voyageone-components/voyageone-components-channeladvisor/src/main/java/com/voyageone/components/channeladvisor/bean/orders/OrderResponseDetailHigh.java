package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OrderResponseDetailHigh complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderResponseDetailHigh">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}OrderResponseDetailMedium">
 *       &lt;sequence>
 *         &lt;element name="ShoppingCart" type="{http://api.channeladvisor.com/datacontracts/orders}OrderCart" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderResponseDetailHigh", propOrder = { "shoppingCart" })
public class OrderResponseDetailHigh extends OrderResponseDetailMedium {

	@XmlElement(name = "ShoppingCart")
	protected OrderCart shoppingCart;

	/**
	 * Gets the value of the shoppingCart property.
	 *
	 * @return possible object is {@link OrderCart }
	 *
	 */
	public OrderCart getShoppingCart() {
		return shoppingCart;
	}

	/**
	 * Sets the value of the shoppingCart property.
	 *
	 * @param value
	 *            allowed object is {@link OrderCart }
	 *
	 */
	public void setShoppingCart(OrderCart value) {
		this.shoppingCart = value;
	}

}
