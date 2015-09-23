package com.voyageone.oms.modelbean;


public class OrderRefundsBean {
	// 网络订单号
	private String sourceOrderId;

	private String originSourceOrderId;

	// 渠道ID
	private String orderChannelId;

	// cart ID
	private String cartId;

	// 退款单编号
	private String refundId;

	// 退款单状态
	private String refundStatus;

	// 退款单阶段
	private String refundPhase;
	
	// 处理标志位
	private boolean processFlag;

	// 退款金额
	private String refundFee;

	// 创建用户
	private String creater;

	// 更新用户
	private String modifier;

	public String getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public String getOriginSourceOrderId() {
		return originSourceOrderId;
	}

	public void setOriginSourceOrderId(String originSourceOrderId) {
		this.originSourceOrderId = originSourceOrderId;
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

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getRefundPhase() {
		return refundPhase;
	}

	public void setRefundPhase(String refundPhase) {
		this.refundPhase = refundPhase;
	}

	public boolean isProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(boolean processFlag) {
		this.processFlag = processFlag;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
}
