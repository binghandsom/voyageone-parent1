package com.voyageone.components.eexpress.bean;

import java.util.List;

public class ExpressTrackingRes{
	
	private String result;
	private String Msg;
	private List<ExpressTrackingDetail> trackingDetails;


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public List<ExpressTrackingDetail> getTrackingDetails() {
		return trackingDetails;
	}

	public void setTrackingDetails(List<ExpressTrackingDetail> trackingDetails) {
		this.trackingDetails = trackingDetails;
	}
}
