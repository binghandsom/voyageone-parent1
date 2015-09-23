package com.voyageone.oms.formbean;


public class OutFormSearch {

	/**
	 * 订单号
	 */
	private String orderNumber;
	
	/**
	 * 源订单号
	 */
	private String sourceOrderId;
	
	/**
	 * 订单日期
	 */
	private String orderDate;
	
	/**
	 * revised grand total
	 */
	private String finalGrandTotal;
	
//	/**
//	 * balancedue
//	 */
//	private String balanceDue;
	
	/**
	 * 支付差额
	 */
	private String paymentBalanceDue;
	
	/**
	 * 交易差额
	 */
	private String transactionBalanceDue;
	
	/**
	 * 订单状态
	 */
	private String orderStatus;
	
	/**
	 * 渠道名
	 */
	private String channel;

	
	/**
	 * 店铺名
	 */
	private String store;
	
	/**
	 * 旺旺ID
	 */
	private String wangwangId;

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the sourceOrderId
	 */
	public String getSourceOrderId() {
		return sourceOrderId;
	}

	/**
	 * @param sourceOrderId the sourceOrderId to set
	 */
	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the finalGrandTotal
	 */
	public String getFinalGrandTotal() {
		return finalGrandTotal;
	}

	/**
	 * @param finalGrandTotal the finalGrandTotal to set
	 */
	public void setFinalGrandTotal(String finalGrandTotal) {
		this.finalGrandTotal = finalGrandTotal;
	}

//	/**
//	 * @return the balanceDue
//	 */
//	public String getBalanceDue() {
//		return balanceDue;
//	}
//
//	/**
//	 * @param balanceDue the balanceDue to set
//	 */
//	public void setBalanceDue(String balanceDue) {
//		this.balanceDue = balanceDue;
//	}

	public String getPaymentBalanceDue() {
		return paymentBalanceDue;
	}

	public void setPaymentBalanceDue(String paymentBalanceDue) {
		this.paymentBalanceDue = paymentBalanceDue;
	}

	public String getTransactionBalanceDue() {
		return transactionBalanceDue;
	}

	public void setTransactionBalanceDue(String transactionBalanceDue) {
		this.transactionBalanceDue = transactionBalanceDue;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}
	
	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * @return the store
	 */
	public String getStore() {
		return store;
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(String store) {
		this.store = store;
	}

	/**
	 * @return the wangwangId
	 */
	public String getWangwangId() {
		return wangwangId;
	}

	/**
	 * @param wangwangId the wangwangId to set
	 */
	public void setWangwangId(String wangwangId) {
		this.wangwangId = wangwangId;
	}
	
}
