package com.voyageone.common.components.eExpress.bean;

public class ExpressTrackingRes{
	
	
	private String result;
	private String shipment_number;
	private String customer_number;
	private String status_code;
	private String description;
	private String entry_datetime;
	private String Msg;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getShipment_number() {
		return shipment_number;
	}
	public void setShipment_number(String shipmentNumber) {
		shipment_number = shipmentNumber;
	}
	public String getCustomer_number() {
		return customer_number;
	}
	public void setCustomer_number(String customerNumber) {
		customer_number = customerNumber;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String statusCode) {
		status_code = statusCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEntry_datetime() {
		return entry_datetime;
	}
	public void setEntry_datetime(String entryDatetime) {
		entry_datetime = entryDatetime;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	
	
	
	
}
