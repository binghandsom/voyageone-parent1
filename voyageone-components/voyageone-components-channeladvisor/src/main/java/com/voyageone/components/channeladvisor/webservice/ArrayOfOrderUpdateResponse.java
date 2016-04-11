package com.voyageone.components.channeladvisor.webservice;

import com.voyageone.components.channeladvisor.webservice.OrderUpdateResponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ArrayOfOrderUpdateResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfOrderUpdateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderUpdateResponse" type="{http://api.channeladvisor.com/webservices/}OrderUpdateResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOrderUpdateResponse", propOrder = { "orderUpdateResponse" })
public class ArrayOfOrderUpdateResponse {

	@XmlElement(name = "OrderUpdateResponse", nillable = true)
	protected List<OrderUpdateResponse> orderUpdateResponse;

	/**
	 * Gets the value of the orderUpdateResponse property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the orderUpdateResponse property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOrderUpdateResponse().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OrderUpdateResponse }
	 * 
	 * 
	 */
	public List<OrderUpdateResponse> getOrderUpdateResponse() {
		if (orderUpdateResponse == null) {
			orderUpdateResponse = new ArrayList<OrderUpdateResponse>();
		}
		return this.orderUpdateResponse;
	}

}
