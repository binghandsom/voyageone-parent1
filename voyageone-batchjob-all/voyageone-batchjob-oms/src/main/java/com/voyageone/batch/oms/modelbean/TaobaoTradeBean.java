package com.voyageone.batch.oms.modelbean;

import java.util.List;


public class TaobaoTradeBean {
	// 订单编号
	protected String tid = "";
	// 购买人昵称    protected String buyer_nick = "";
    // 成交时间
    protected String created = "";
    // 支付时间
    protected String pay_time = "";
    // 收件人名字    protected String receiver_name = "";
    // 收件地邮编    protected String receiver_zip = "";
    // 收件地省份    protected String receiver_state = "";
    // 收件地城市    protected String receiver_city = "";
    // 收件地区
    protected String receiver_district = "";
    // 收件地具体地址
    protected String receiver_address = "";
    // 收件人手机    protected String receiver_mobile = "";
    // 收件人座机    protected String receiver_phone = "";
    // 买家电子邮件
    protected String buyer_email = "";
    // 买家留言
    protected String buyer_message = "";
    // 支付宝交易编号    protected String alipay_no = "";
    // 邮费
    protected String post_fee = "";
    // 物流方式
    protected String shipping_type = "";
    // 实付金额
    protected String payment = "";
    // 商品金额
    protected String total_fee = "";
    // 使用积分
    protected String point_fee = "";
    // 订单来源
    protected String target = "";
    // 订单信息XML
    protected String orderXML = "";
    // 订单状态    protected String status = "";
    
	protected String shop_name = "";
    protected String order_channel_id = "";
    
    // 退款编号
    protected String rid = "";  
    // 评价内容
    protected String content = "";
    // 订单信息
    protected List<TaobaoOrderBean> order;
    // 折扣活动信息
    protected List<TaobaoPromotionBean> promotion;
    // 买家支付宝账号
    protected String buyer_alipay_no = "";
    // 订单cartId
    protected String cartId = "";
    // 订单channelId
    protected String channelId = "";
    // 发票信息
    protected String invoice_info = "";
	// 订单修改时间
	protected String modified = "";
	// 支付方式
	protected String paymentMethod = "";
	// 订单物品列表
	protected String product_sku = "";
	// 订单物品数量列表
	protected String product_quantity = "";
	// 身份证号
	protected String id_card = "";

    /**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}
	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}
	/**
	 * @return the buyer_nick
	 */
	public String getBuyer_nick() {
		return buyer_nick;
	}
	/**
	 * @param buyer_nick the buyer_nick to set
	 */
	public void setBuyer_nick(String buyer_nick) {
		this.buyer_nick = buyer_nick;
	}
	/**
	 * @return the created
	 */
	public String getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = created;
	}
	/**
	 * @return the pay_time
	 */
	public String getPay_time() {
		return pay_time;
	}
	/**
	 * @param pay_time the pay_time to set
	 */
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	/**
	 * @return the receiver_name
	 */
	public String getReceiver_name() {
		return receiver_name;
	}
	/**
	 * @param receiver_name the receiver_name to set
	 */
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	/**
	 * @return the receiver_zip
	 */
	public String getReceiver_zip() {
		return receiver_zip;
	}
	/**
	 * @param receiver_zip the receiver_zip to set
	 */
	public void setReceiver_zip(String receiver_zip) {
		this.receiver_zip = receiver_zip;
	}
	/**
	 * @return the receiver_state
	 */
	public String getReceiver_state() {
		return receiver_state;
	}
	/**
	 * @param receiver_state the receiver_state to set
	 */
	public void setReceiver_state(String receiver_state) {
		this.receiver_state = receiver_state;
	}
	/**
	 * @return the receiver_city
	 */
	public String getReceiver_city() {
		return receiver_city;
	}
	/**
	 * @param receiver_city the receiver_city to set
	 */
	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}
	/**
	 * @return the receiver_district
	 */
	public String getReceiver_district() {
		return receiver_district;
	}
	/**
	 * @param receiver_district the receiver_district to set
	 */
	public void setReceiver_district(String receiver_district) {
		this.receiver_district = receiver_district;
	}
	/**
	 * @return the receiver_address
	 */
	public String getReceiver_address() {
		return receiver_address;
	}
	/**
	 * @param receiver_address the receiver_address to set
	 */
	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}
	/**
	 * @return the receiver_mobile
	 */
	public String getReceiver_mobile() {
		return receiver_mobile;
	}
	/**
	 * @param receiver_mobile the receiver_mobile to set
	 */
	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}
	/**
	 * @return the receiver_phone
	 */
	public String getReceiver_phone() {
		return receiver_phone;
	}
	/**
	 * @param receiver_phone the receiver_phone to set
	 */
	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}
	/**
	 * @return the buyer_email
	 */
	public String getBuyer_email() {
		return buyer_email;
	}
	/**
	 * @param buyer_email the buyer_email to set
	 */
	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}
	/**
	 * @return the buyer_message
	 */
	public String getBuyer_message() {
		return buyer_message;
	}
	/**
	 * @param buyer_message the buyer_message to set
	 */
	public void setBuyer_message(String buyer_message) {
		this.buyer_message = buyer_message;
	}
	/**
	 * @return the alipay_no
	 */
	public String getAlipay_no() {
		return alipay_no;
	}
	/**
	 * @param alipay_no the alipay_no to set
	 */
	public void setAlipay_no(String alipay_no) {
		this.alipay_no = alipay_no;
	}
	/**
	 * @return the post_fee
	 */
	public String getPost_fee() {
		return post_fee;
	}
	/**
	 * @param post_fee the post_fee to set
	 */
	public void setPost_fee(String post_fee) {
		this.post_fee = post_fee;
	}
	/**
	 * @return the shipping_type
	 */
	public String getShipping_type() {
		return shipping_type;
	}
	/**
	 * @param shipping_type the shipping_type to set
	 */
	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}
	/**
	 * @return the payment
	 */
	public String getPayment() {
		return payment;
	}
	/**
	 * @param payment the payment to set
	 */
	public void setPayment(String payment) {
		this.payment = payment;
	}
	/**
	 * @return the total_fee
	 */
	public String getTotal_fee() {
		return total_fee;
	}
	/**
	 * @param total_fee the total_fee to set
	 */
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	/**
	 * @return the point_fee
	 */
	public String getPoint_fee() {
		return point_fee;
	}
	/**
	 * @param point_fee the point_fee to set
	 */
	public void setPoint_fee(String point_fee) {
		this.point_fee = point_fee;
	}
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return the orderXML
	 */
	public String getOrderXML() {
		return orderXML;
	}
	/**
	 * @param orderXML the orderXML to set
	 */
	public void setOrderXML(String orderXML) {
		this.orderXML = orderXML;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the shop_name
	 */
	public String getShop_name() {
		return shop_name;
	}
	/**
	 * @param shop_name the shop_name to set
	 */
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	/**
	 * @return the order_channel_id
	 */
	public String getOrder_channel_id() {
		return order_channel_id;
	}
	/**
	 * @param order_channel_id the order_channel_id to set
	 */
	public void setOrder_channel_id(String order_channel_id) {
		this.order_channel_id = order_channel_id;
	}
	/**
	 * @return the rid
	 */
	public String getRid() {
		return rid;
	}
	/**
	 * @param rid the rid to set
	 */
	public void setRid(String rid) {
		this.rid = rid;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the order
	 */
	public List<TaobaoOrderBean> getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(List<TaobaoOrderBean> order) {
		this.order = order;
	}
	/**
	 * @return the promotion
	 */
	public List<TaobaoPromotionBean> getPromotion() {
		return promotion;
	}
	/**
	 * @param promotion the promotion to set
	 */
	public void setPromotion(List<TaobaoPromotionBean> promotion) {
		this.promotion = promotion;
	}
	/**
	 * @return the buyer_alipay_no
	 */
	public String getBuyer_alipay_no() {
		return buyer_alipay_no;
	}
	/**
	 * @param buyer_alipay_no the buyer_alipay_no to set
	 */
	public void setBuyer_alipay_no(String buyer_alipay_no) {
		this.buyer_alipay_no = buyer_alipay_no;
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
	 * @return the invoice_info
	 */
	public String getInvoice_info() {
		return invoice_info;
	}
	/**
	 * @param invoice_info the invoice_info to set
	 */
	public void setInvoice_info(String invoice_info) {
		this.invoice_info = invoice_info;
	}
	/**
	 * @return the modified
	 */
	public String getModified() {
		return modified;
	}
	/**
	 * @param modified the modified to set
	 */
	public void setModified(String modified) {
		this.modified = modified;
	}
	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getProduct_sku() {
		return product_sku;
	}
	public void setProduct_sku(String product_sku) {
		this.product_sku = product_sku;
	}
	public String getProduct_quantity() {
		return product_quantity;
	}
	public void setProduct_quantity(String product_quantity) {
		this.product_quantity = product_quantity;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	
}
