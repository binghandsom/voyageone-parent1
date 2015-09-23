package com.voyageone.batch.oms.bean;

public class JsonVerificationBean {
	
	// Json数据
	protected String jsonData;
	
	// 做成时间
	protected String timeStamp;
	
	// 签名
	protected String signature;

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
