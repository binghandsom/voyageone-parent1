package com.voyageone.oms.formbean;


public class OutFormUploadSettlementFile {
	//	文件名
	private String settlementFileId;
	//	处理件数
	private String uploadRecCount;
	//	渠道名称
	private String orderChannelName;
	//	店铺名
	private String cartName;

	public String getSettlementFileId() {
		return settlementFileId;
	}

	public void setSettlementFileId(String settlementFileId) {
		this.settlementFileId = settlementFileId;
	}

	public String getUploadRecCount() {
		return uploadRecCount;
	}

	public void setUploadRecCount(String uploadRecCount) {
		this.uploadRecCount = uploadRecCount;
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
}
