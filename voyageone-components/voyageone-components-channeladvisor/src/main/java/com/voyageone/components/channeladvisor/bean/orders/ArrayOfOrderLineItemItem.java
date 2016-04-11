package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Java class for ArrayOfOrderLineItemItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfOrderLineItemItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderLineItemItem" type="{http://api.channeladvisor.com/datacontracts/orders}OrderLineItemItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOrderLineItemItem", propOrder = { "orderLineItemItem" })
public class ArrayOfOrderLineItemItem {

	@XmlElement(name = "OrderLineItemItem", nillable = true)
	protected List<OrderLineItemItem> orderLineItemItem;

	/**
	 * Gets the value of the orderLineItemItem property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the orderLineItemItem property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getOrderLineItemItem().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OrderLineItemItem }
	 *
	 *
	 */
	public List<OrderLineItemItem> getOrderLineItemItem() {
		if (orderLineItemItem == null) {
			orderLineItemItem = new ArrayList<OrderLineItemItem>();
		}
		return this.orderLineItemItem;
	}

}
