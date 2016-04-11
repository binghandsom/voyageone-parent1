package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.*;

/**
 * <p>
 * Java class for ItemWeight complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ItemWeight">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
 *       &lt;attribute name="UnitOfMeasure" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemWeight", propOrder = { "value" })
public class ItemWeight {

	@XmlValue
	protected double value;
	@XmlAttribute(name = "UnitOfMeasure")
	protected String unitOfMeasure;

	/**
	 * Gets the value of the value property.
	 * 
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Gets the value of the unitOfMeasure property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	/**
	 * Sets the value of the unitOfMeasure property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUnitOfMeasure(String value) {
		this.unitOfMeasure = value;
	}

}
