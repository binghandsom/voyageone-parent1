package com.voyageone.components.eexpress.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("eExpress_shipment_cancel")
public class ExpressShipmentCancelReq {
	
	// 标记为节点属性  
    @XStreamAsAttribute  
    protected String xmlns = "http://tempuri.org/";  
    
	private String shipmentNo;
	private String userToken;
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getShipmentNo() {
		return shipmentNo;
	}
	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
	
	
}
