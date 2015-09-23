package com.voyageone.batch.oms.modelbean;

public class TradeInfoBean {

	// 订单来源
	protected String target;
	// 订单编号
	protected String tid;
	// 订单状态	protected String status;
	// 订单修改时间
	protected String modified;
	// 买家昵称
	protected String buyer_nick;
	// oid
	protected String oid;
	// rid
	protected String rid;
	// fee
	protected String fee;
	// 订单cartId
	protected String cartId;
	// 订单channelId
	protected String channelId;
	// 退款阶段
    protected String refundPhase;
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getBuyer_nick() {
		return buyer_nick;
	}
	public void setBuyer_nick(String buyerNick) {
		buyer_nick = buyerNick;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	/**
	 * @return the cartId
	 */
	public String getCartId() {
		return cartId;
	}
	/**
	 * @param cartId the cartId to set
	 */
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	 * @return the refundPhase
	 */
	public String getRefundPhase() {
		return refundPhase;
	}
	/**
	 * @param refundPhase the refundPhase to set
	 */
	public void setRefundPhase(String refundPhase) {
		this.refundPhase = refundPhase;
	}
}
