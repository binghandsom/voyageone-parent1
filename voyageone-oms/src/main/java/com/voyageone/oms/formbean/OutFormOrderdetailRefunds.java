package com.voyageone.oms.formbean;

public class OutFormOrderdetailRefunds {
	
	/**
	 * Key
	 */
	private String id;
	
	/**
	 * 元订单号
	 */
	private String sourceOrderId;
	
	/**
	 * 订单渠道
	 */
	private String orderChannelId;
	
	/**
	 * Cart id
	 */
	private String cartId;
	
	/**
	 * 退款编号
	 */
	private String refundId;

	/**
	 * 退款状态
	 */
	private String refundStatus;
	
	/**
	 * 申请时间
	 */
	private String refundTime;
	
	/**
	 * 退款类型
	 */
	private String refundKind;
	
	/**
	 * 退款金额
	 */
	private String refundFee;
	
	/**
	 * 退款原因
	 */
	private String refundReason;
	
	/**
	 * 退款说明
	 */
	private String refundComment;
	
	/**
	 * 退款阶段
	 */
	private String refundPhase;
	
	/**
	 * 处理标志位
	 */
	private boolean processFlag;
	
	/**
	 * 货币金额
	 */
	private String currencyType;
	
	/**
	 * 画面传入
	 */
	/**
	 * 		图片（Base64）
	 * 			taobao.refund.refuse 卖家拒绝退款
	 * 			taobao.rp.returngoods.refuse 卖家拒绝退货
	 */
	private String image;
	
	/**
	 * 		留言内容（共用）
	 * 			taobao.refund.refuse 卖家拒绝退款		（refuse_message ：拒绝退款说明）
	 * 			taobao.rp.returngoods.agree 卖家同意退货 （remark ：卖家退货留言）
	 * 			taobao.rp.refund.review 审核退款单		（message ： 审核留言）
	 */
	private String content;
	
	/**
	 * 		短信验证码（taobao.rp.refunds.agree 同意退款）
	 */
	private String code;
	
	/**
	 * 		主订单号（详情画面传入）
	 */
	private String orderNumber;
	
	/**
	 * 		物流公司运单号（taobao.rp.returngoods.refill 卖家回填物流信息）
	 */
	private String logisticsWaybillNo;
	
	/**
	 * 		物流公司编号（taobao.rp.returngoods.refill 卖家回填物流信息）
	 */
	private String logisticsCompanyCode;
	
	/**
	 * 		审核结果（taobao.rp.refund.review 审核退款单）
	 */
	private boolean reviewResult;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
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

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundKind() {
		return refundKind;
	}

	public void setRefundKind(String refundKind) {
		this.refundKind = refundKind;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getRefundComment() {
		return refundComment;
	}

	public void setRefundComment(String refundComment) {
		this.refundComment = refundComment;
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

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getLogisticsWaybillNo() {
		return logisticsWaybillNo;
	}

	public void setLogisticsWaybillNo(String logisticsWaybillNo) {
		this.logisticsWaybillNo = logisticsWaybillNo;
	}

	public String getLogisticsCompanyCode() {
		return logisticsCompanyCode;
	}

	public void setLogisticsCompanyCode(String logisticsCompanyCode) {
		this.logisticsCompanyCode = logisticsCompanyCode;
	}

	public boolean isReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(boolean reviewResult) {
		this.reviewResult = reviewResult;
	}
}
