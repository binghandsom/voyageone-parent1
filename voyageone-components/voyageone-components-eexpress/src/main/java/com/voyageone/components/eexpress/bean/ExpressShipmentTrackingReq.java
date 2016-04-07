package com.voyageone.components.eexpress.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("eExpress_shipment_tracking")
public class ExpressShipmentTrackingReq {
	
	// 标记为节点属性  
    @XStreamAsAttribute  
    protected String xmlns = "http://tempuri.org/";  
    
	private String shipment_number;
	private String userToken;
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getShipment_number() {
		return shipment_number;
	}
	public void setShipment_number(String shipmentNumber) {
		shipment_number = shipmentNumber;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	
	
}
