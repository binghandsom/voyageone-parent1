package com.voyageone.oms.formbean;

public class OutFormOrderdetailRefundsStatus {
	
	/**
	 * 退款编号
	 */
	private String refundId;

	/**
	 * 阿里用
	 */
	/**
	 * 		是否发货
	 */
	private boolean isShipped;
	
	/**
	 * 		是否需要退货
	 */
	private boolean isHasGoodReturn;

	/**
	 * 		退款状态
	 */
	private String status;

	/**
	 * 		同意退款返回Code（10000：发送二次验证短信成功	40000：操作成功）
	 */
	private String msgCode;

	/**
	 * 阿里以外用
	 */
	/**
	 * 		是否处理结束
	 */
	private boolean isEndProcess;

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public boolean isShipped() {
		return isShipped;
	}

	public void setShipped(boolean isShipped) {
		this.isShipped = isShipped;
	}

	public boolean isHasGoodReturn() {
		return isHasGoodReturn;
	}

	public void setHasGoodReturn(boolean isHasGoodReturn) {
		this.isHasGoodReturn = isHasGoodReturn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public boolean isEndProcess() {
		return isEndProcess;
	}

	public void setIsEndProcess(boolean isEndProcess) {
		this.isEndProcess = isEndProcess;
	}
}
