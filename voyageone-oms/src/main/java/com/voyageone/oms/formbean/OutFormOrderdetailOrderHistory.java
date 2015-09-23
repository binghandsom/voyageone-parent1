package com.voyageone.oms.formbean;

import java.util.ArrayList;
import java.util.List;

public class OutFormOrderdetailOrderHistory {

//	/**
//	 * 订单日期
//	 */
//	private String orderDate;
//	
//	/**
//	 * 订单号
//	 */
//	private List<String> orderNumbers;
//
//	public String getOrderDate() {
//		return orderDate;
//	}
//
//	public void setOrderDate(String orderDate) {
//		this.orderDate = orderDate;
//	}
//
//	public List<String> getOrderNumbers() {
//		return orderNumbers;
//	}
//
//	public void setOrderNumbers(List<String> orderNumbers) {
//		this.orderNumbers = orderNumbers;
//	}
	
	// 子订单对应
	/**
	 * 订单日期
	 */
	private String orderDate;
	
	/**
	 * 订单号
	 */
	private List<OutFormOrderdetailOrderHistoryItem> orderNumbers;

	public OutFormOrderdetailOrderHistory() {
		this.orderNumbers = new ArrayList<OutFormOrderdetailOrderHistoryItem>();
	}
	
	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public List<OutFormOrderdetailOrderHistoryItem> getOrderNumbers() {
		return orderNumbers;
	}

	public void setOrderNumbers(
			List<OutFormOrderdetailOrderHistoryItem> orderNumbers) {
		this.orderNumbers = orderNumbers;
	}
	
}
