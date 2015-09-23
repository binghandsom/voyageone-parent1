package com.voyageone.oms.formbean;

import java.util.ArrayList;
import java.util.List;

public class OutFormOrderdetailOrderHistoryItem {

	/**
	 * 订单号（原始订单）
	 */
	private String orderNumber;
	
	/**
	 * 网络订单号
	 */
	private String sourceOrderId;
	
	/**
	 * 子订单号
	 */
	private List<String> sonOrderNumbers;

	public OutFormOrderdetailOrderHistoryItem() {
		this.sonOrderNumbers = new ArrayList<String>();
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public List<String> getSonOrderNumbers() {
		return sonOrderNumbers;
	}

	public void setSonOrderNumbers(List<String> sonOrderNumbers) {
		this.sonOrderNumbers = sonOrderNumbers;
	}	
}
