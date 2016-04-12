package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

/**
 * <p>
 * Java class for OrderLineItemBase complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OrderLineItemBase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LineItemType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UnitPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderLineItemBase", propOrder = { "lineItemType", "unitPrice" })
public abstract class OrderLineItemBase {

	@XmlElement(name = "LineItemType")
	protected String lineItemType;
	@XmlElement(name = "UnitPrice", required = true)
	protected BigDecimal unitPrice;

	/**
	 * Gets the value of the lineItemType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLineItemType() {
		return lineItemType;
	}

	/**
	 * Sets the value of the lineItemType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLineItemType(String value) {
		this.lineItemType = value;
	}

	/**
	 * Gets the value of the unitPrice property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * Sets the value of the unitPrice property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setUnitPrice(BigDecimal value) {
		this.unitPrice = value;
	}

}
