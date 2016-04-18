package com.voyageone.components.eexpress.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

@XStreamAlias("eExpress_shipment_import")
public class ExpressShipmentImportReq {
	
	// 标记为节点属性  
    @XStreamAsAttribute  
    protected String xmlns = "http://tempuri.org/";  
    
	private Awb awb;	
	private List<AwbDetail> objAwbDetail;
	
	public Awb getAwb() {
		return awb;
	}
	public void setAwb(Awb awb) {
		this.awb = awb;
	}
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public List<AwbDetail> getObjAwbDetail() {
		return objAwbDetail;
	}
	public void setObjAwbDetail(List<AwbDetail> objAwbDetail) {
		this.objAwbDetail = objAwbDetail;
	}	


	
}
