/**
 * 
 */
package com.voyageone.batch.oms.modelbean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author jacky
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "itemNumber","sku","status","quantityOrdered","quantityShipped","quantityReturned","quantityNeeded"})
public class SelfOrderDetailInfo4Post {
	@XmlTransient
	private String orderNumber;
	@XmlElement(name = "ItemNumber")
	private String itemNumber;
	@XmlElement(name = "Sku")
	private String sku;
	@XmlElement(name = "Status")
	private String status;
	@XmlElement(name = "QuantityOrdered")
	private String quantityOrdered;
	@XmlElement(name = "QuantityShipped")
	private String quantityShipped;
	@XmlElement(name = "QuantityReturned")
	private String quantityReturned;
	@XmlElement(name = "QuantityNeeded")
	private String quantityNeeded;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getQuantityOrdered() {
		return quantityOrdered;
	}
	public void setQuantityOrdered(String quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	public String getQuantityShipped() {
		return quantityShipped;
	}
	public void setQuantityShipped(String quantityShipped) {
		this.quantityShipped = quantityShipped;
	}
	public String getQuantityReturned() {
		return quantityReturned;
	}
	public void setQuantityReturned(String quantityReturned) {
		this.quantityReturned = quantityReturned;
	}
	public String getQuantityNeeded() {
		return quantityNeeded;
	}
	public void setQuantityNeeded(String quantityNeeded) {
		this.quantityNeeded = quantityNeeded;
	}
}
