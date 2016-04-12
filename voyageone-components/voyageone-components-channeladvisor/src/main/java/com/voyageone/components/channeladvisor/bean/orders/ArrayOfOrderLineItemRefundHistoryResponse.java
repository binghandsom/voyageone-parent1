package com.voyageone.components.channeladvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Java class for ArrayOfOrderLineItemRefundHistoryResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfOrderLineItemRefundHistoryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderLineItemRefundHistoryResponse" type="{http://api.channeladvisor.com/datacontracts/orders}OrderLineItemRefundHistoryResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOrderLineItemRefundHistoryResponse", propOrder = { "orderLineItemRefundHistoryResponse" })
public class ArrayOfOrderLineItemRefundHistoryResponse {

	@XmlElement(name = "OrderLineItemRefundHistoryResponse", nillable = true)
	protected List<OrderLineItemRefundHistoryResponse> orderLineItemRefundHistoryResponse;

	/**
	 * Gets the value of the orderLineItemRefundHistoryResponse property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the orderLineItemRefundHistoryResponse
	 * property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getOrderLineItemRefundHistoryResponse().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OrderLineItemRefundHistoryResponse }
	 *
	 *
	 */
	public List<OrderLineItemRefundHistoryResponse> getOrderLineItemRefundHistoryResponse() {
		if (orderLineItemRefundHistoryResponse == null) {
			orderLineItemRefundHistoryResponse = new ArrayList<OrderLineItemRefundHistoryResponse>();
		}
		return this.orderLineItemRefundHistoryResponse;
	}

}
