package com.voyageone.oms.formbean;

public class OutFormCustomerOrders {

	private String orderNumber;
	private String sourceOrderId;
	private String marketName;
	private String orderDate;
	private String finalGrandTotal;
	private String balanceDue;
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}
	
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public void setFinalGrandTotal(String finalGrandTotal) {
		this.finalGrandTotal = finalGrandTotal;
	}
	
	public void setBalanceDue(String balanceDue) {
		this.balanceDue = balanceDue;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	
	public String getSourceOrderId() {
		return sourceOrderId;
	}
	
	public String getMarketName() {
		return marketName;
	}
	
	public String getOrderDate() {
		return orderDate;
	}
	
	public String getFinalGrandTotal() {
		return finalGrandTotal;
	}
	
	public String getBalanceDue() {
		return balanceDue;
	}

}
