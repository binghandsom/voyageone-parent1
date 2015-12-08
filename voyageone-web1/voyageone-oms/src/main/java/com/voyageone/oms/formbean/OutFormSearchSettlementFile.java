package com.voyageone.oms.formbean;


public class OutFormSearchSettlementFile {

	private String orderChannelId;
	//	orderChannelName
	private String orderChannelName;

	private String cartId;
	//	销售渠道
	private String cartName;
	//	文件名
	private String settlementFileId;
	//	上传时间
	private String uploadTime;
	//	收入合计
	private String totalIncome;
	//	支出合计
	private String totalExpense;

	public String getSettlementFileId() {
		return settlementFileId;
	}

	public void setSettlementFileId(String settlementFileId) {
		this.settlementFileId = settlementFileId;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getOrderChannelName() {
		return orderChannelName;
	}

	public void setOrderChannelName(String orderChannelName) {
		this.orderChannelName = orderChannelName;
	}

	public String getCartName() {
		return cartName;
	}

	public void setCartName(String cartName) {
		this.cartName = cartName;
	}

	public String getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}

	public String getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(String totalExpense) {
		this.totalExpense = totalExpense;
	}

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
}
