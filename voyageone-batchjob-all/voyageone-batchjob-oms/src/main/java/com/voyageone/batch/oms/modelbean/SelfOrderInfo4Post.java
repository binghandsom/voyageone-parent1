/**
 * 
 */
package com.voyageone.batch.oms.modelbean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author jacky
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "sourceOrderId","status","carrier","trackingNumber","items"})
public class SelfOrderInfo4Post {
	
	@XmlElement(name = "OrderNumber")
	private String sourceOrderId;
	@XmlTransient
	private String orderNumber;
	@XmlElement(name = "Status")
	private String status;
	@XmlElement(name = "Carrier")
	private String carrier;
	@XmlElement(name = "TrackingNumber")
	private String trackingNumber;
	@XmlTransient
	private String orderDateTime;
	@XmlElementWrapper(name = "Items")
	@XmlElement(name = "Item")
	private List<SelfOrderDetailInfo4Post> items;
	
	public String getSourceOrderId() {
		return sourceOrderId;
	}
	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	public List<SelfOrderDetailInfo4Post> getItems() {
		return items;
	}
	public void setItems(List<SelfOrderDetailInfo4Post> items) {
		this.items = items;
	}
}
