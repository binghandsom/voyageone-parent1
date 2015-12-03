package com.voyageone.oms.formbean;

public class OutFormCustomerTransactions {

	private String order_number;
	private String customer_id;
	private String date;
	private String description;
	private String amount;
	private String type;
	
	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getOrder_number() {
		return order_number;
	}
	
	public String getCustomer_id() {
		return customer_id;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public String getType() {
		return type;
	}
	
}
