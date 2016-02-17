package com.voyageone.batch.oms.bean;

public class JasonCommentBean {
	// 订单编号
	protected String tid;
    // 支付时间
    protected String pay_time;
    // 收件人名字
    protected String receiver_name;
    // 收件人手机
    protected String receiver_mobile;
    // 店铺名
    protected String shop_name;
    // 订单渠道
    protected String order_channel_id;
    // 付款金额
    protected String payment;
    // 产品数量
    protected String product_num;
	// 购买人昵称
    protected String buyer_nick;
    // 身份证号
    protected String id_card;
	// cartId
	protected String cartId;
    
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String payTime) {
		pay_time = payTime;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiverName) {
		receiver_name = receiverName;
	}
	public String getReceiver_mobile() {
		return receiver_mobile;
	}
	public void setReceiver_mobile(String receiverMobile) {
		receiver_mobile = receiverMobile;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shopName) {
		shop_name = shopName;
	}
	public String getOrder_channel_id() {
		return order_channel_id;
	}
	public void setOrder_channel_id(String orderChannelId) {
		order_channel_id = orderChannelId;
	}
	public String getProduct_num() {
		return product_num;
	}
	public void setProduct_num(String productNum) {
		product_num = productNum;
	}
	/**
	 * @return the buyer_nick
	 */
	public String getBuyer_nick() {
		return buyer_nick;
	}
	/**
	 * @param buyerNick the buyer_nick to set
	 */
	public void setBuyer_nick(String buyerNick) {
		buyer_nick = buyerNick;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
}
