package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Java class for ArrayOfOrderLineItemItemPromo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfOrderLineItemItemPromo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderLineItemItemPromo" type="{http://api.channeladvisor.com/datacontracts/orders}OrderLineItemItemPromo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOrderLineItemItemPromo", propOrder = { "orderLineItemItemPromo" })
public class ArrayOfOrderLineItemItemPromo {

	@XmlElement(name = "OrderLineItemItemPromo", nillable = true)
	protected List<OrderLineItemItemPromo> orderLineItemItemPromo;

	/**
	 * Gets the value of the orderLineItemItemPromo property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the orderLineItemItemPromo property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOrderLineItemItemPromo().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OrderLineItemItemPromo }
	 * 
	 * 
	 */
	public List<OrderLineItemItemPromo> getOrderLineItemItemPromo() {
		if (orderLineItemItemPromo == null) {
			orderLineItemItemPromo = new ArrayList<OrderLineItemItemPromo>();
		}
		return this.orderLineItemItemPromo;
	}

}
