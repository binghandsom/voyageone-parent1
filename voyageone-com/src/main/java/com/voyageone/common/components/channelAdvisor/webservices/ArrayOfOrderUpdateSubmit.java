package com.voyageone.common.components.channelAdvisor.webservices;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ArrayOfOrderUpdateSubmit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfOrderUpdateSubmit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderUpdateSubmit" type="{http://api.channeladvisor.com/webservices/}OrderUpdateSubmit" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOrderUpdateSubmit", propOrder = { "orderUpdateSubmit" })
public class ArrayOfOrderUpdateSubmit {

	@XmlElement(name = "OrderUpdateSubmit", nillable = true)
	protected List<OrderUpdateSubmit> orderUpdateSubmit;

	/**
	 * Gets the value of the orderUpdateSubmit property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the orderUpdateSubmit property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOrderUpdateSubmit().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OrderUpdateSubmit }
	 * 
	 * 
	 */
	public List<OrderUpdateSubmit> getOrderUpdateSubmit() {
		if (orderUpdateSubmit == null) {
			orderUpdateSubmit = new ArrayList<>();
		}
		return this.orderUpdateSubmit;
	}

}
