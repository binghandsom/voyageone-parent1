package com.voyageone.batch.oms.formbean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.voyageone.common.util.StringUtils;

public class MagentoTradeBean {

	// 订单编号
	protected String tid;
	// 成交时间
    protected String createdTime;
	// 购买人昵称
    protected String buyer_nick;
    // 买家电话
    protected String buyer_phone;
	// 买家电子邮件
    protected String buyer_email;
    // 买家公司
    protected String buyer_company;
    // 买家留言
    protected String buyer_message;
    // 买家地址
    protected String buyer_address;
    // 买家地区
    protected String buyer_district;
    /**
	 * @return the buyer_district
	 */
	public String getBuyer_district() {
		return buyer_district;
	}
	/**
	 * @param buyer_district the buyer_district to set
	 */
	public void setBuyer_district(String buyer_district) {
		this.buyer_district = buyer_district;
	}
	// 买家城市
    protected String buyer_city;
    // 买家省份
    protected String buyer_state;
    // 买家邮编
    protected String buyer_zip;
    // 买家国家
    protected String buyer_country;
    // 收件人名字
    protected String receiver_name;
    // 收件人公司
    protected String receiver_company;
    // 收件人手机
    protected String receiver_mobile;
    // 收件人邮箱
    protected String receiver_email;
    // 收件地区
    protected String receiver_district;
	// 收件地具体地址
    protected String receiver_address;
    // 收件地城市
    protected String receiver_city;
    // 收件地省份
    protected String receiver_state;
    // 收件地邮编
    protected String receiver_zip;
    // 收件国家
    protected String receiver_country;
    // 支付方式名称
    protected String payment_name;    
    // 支付交易编号
    protected String payment_no;
    // 支付时间
    protected String payment_time;
    // 支付交易描述
    protected String payment_description;
    // 商品金额
    protected String total_fee;
    // 实付金额
    protected String pay_fee;
    // 消费税金额
    protected String tax_fee;
    // 邮费
    protected String shipping_fee;
    // 物流方式
    protected String shipping_type;
    // 折扣信息
    protected List<MagentoCouponBean> coupon = new ArrayList<MagentoCouponBean>();
    // 礼品卡信息
    protected List<MagentoGiftcardBean> giftcard = new ArrayList<MagentoGiftcardBean>();
    // 使用积分
    protected String point_fee;
    // 其他信息IP地址
    protected String other_iphost;
    // 其他信息总重量
    protected String other_totalweight;
    // 其他信息礼物信息
    protected String other_giftmessage;
    // 其他信息备注
    protected String other_comments;
    // 订单信息
    protected List<MagentoOrderBean> order = new ArrayList<MagentoOrderBean>();
    
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getCreatedTime() {
		
		return this.createdTime;
//	    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//	    String t="";
//	    try {
//	    	Date date=new Date(createdTime);
//	    	t=sdf2.format(date);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	    
//		return t;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getBuyer_nick() {
		return buyer_nick;
	}
	public void setBuyer_nick(String buyerNick) {
		buyer_nick = buyerNick;
	}
	public String getBuyer_phone() {
		return buyer_phone;
	}
	public void setBuyer_phone(String buyerPhone) {
		buyer_phone = buyerPhone;
	}
	public String getBuyer_email() {
		return buyer_email;
	}
	public void setBuyer_email(String buyerEmail) {
		buyer_email = buyerEmail;
	}
	public String getBuyer_company() {
		return buyer_company;
	}
	public void setBuyer_company(String buyerCompany) {
		buyer_company = buyerCompany;
	}
	public String getBuyer_message() {
		return buyer_message;
	}
	public void setBuyer_message(String buyerMessage) {
		buyer_message = buyerMessage;
	}
	public String getBuyer_address() {
		return buyer_address;
	}
	public void setBuyer_address(String buyerAddress) {
		buyer_address = buyerAddress;
	}
	public String getBuyer_city() {
		return buyer_city;
	}
	public void setBuyer_city(String buyerCity) {
		buyer_city = buyerCity;
	}
	public String getBuyer_state() {
		return buyer_state;
	}
	public void setBuyer_state(String buyerState) {
		buyer_state = buyerState;
	}
	public String getBuyer_zip() {
		return buyer_zip;
	}
	public void setBuyer_zip(String buyerZip) {
		buyer_zip = buyerZip;
	}
	public String getBuyer_country() {
		return buyer_country;
	}
	public void setBuyer_country(String buyerCountry) {
		buyer_country = buyerCountry;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiverName) {
		receiver_name = receiverName;
	}
	public String getReceiver_company() {
		return receiver_company;
	}
	public void setReceiver_company(String receiverCompany) {
		receiver_company = receiverCompany;
	}
	public String getReceiver_mobile() {
		return receiver_mobile;
	}
	public void setReceiver_mobile(String receiverMobile) {
		receiver_mobile = receiverMobile;
	}
	public String getReceiver_email() {
		return receiver_email;
	}
	public void setReceiver_email(String receiverEmail) {
		receiver_email = receiverEmail;
	}
	public String getReceiver_address() {
		return receiver_address;
	}
	public void setReceiver_address(String receiverAddress) {
		receiver_address = receiverAddress;
	}
	public String getReceiver_city() {
		return receiver_city;
	}
	public void setReceiver_city(String receiverCity) {
		receiver_city = receiverCity;
	}
	public String getReceiver_state() {
		return receiver_state;
	}
	public void setReceiver_state(String receiverState) {
		receiver_state = receiverState;
	}
	public String getReceiver_zip() {
		return receiver_zip;
	}
	public void setReceiver_zip(String receiverZip) {
		receiver_zip = receiverZip;
	}
	public String getReceiver_country() {
		return receiver_country;
	}
	public void setReceiver_country(String receiverCountry) {
		receiver_country = receiverCountry;
	}
	public String getPayment_name() {
		return payment_name;
	}
	public void setPayment_name(String paymentName) {
		payment_name = paymentName;
	}
	public String getPayment_no() {
		return payment_no;
	}
	public void setPayment_no(String paymentNo) {
		payment_no = paymentNo;
	}
	public String getPayment_time() {
		return payment_time;
	}
	public void setPayment_time(String paymentTime) {
		payment_time = paymentTime;
	}
	public String getPayment_description() {
		return payment_description;
	}
	public void setPayment_description(String paymentDescription) {
		payment_description = paymentDescription;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String totalFee) {
		total_fee = totalFee;
	}
	public String getPay_fee() {
		return pay_fee;
	}
	public void setPay_fee(String payFee) {
		pay_fee = payFee;
	}
	public String getTax_fee() {
		return tax_fee;
	}
	public void setTax_fee(String taxFee) {
		tax_fee = taxFee;
	}
	public String getShipping_fee() {
		return shipping_fee;
	}
	public void setShipping_fee(String shippingFee) {
		shipping_fee = shippingFee;
	}
	public String getShipping_type() {
		return shipping_type;
	}
	public void setShipping_type(String shippingType) {
		shipping_type = shippingType;
	}
	public List<MagentoCouponBean> getCoupon() {
		return coupon;
	}
	public void setCoupon(List<MagentoCouponBean> coupon) {
		this.coupon = coupon;
	}
	public List<MagentoGiftcardBean> getGiftcard() {
		return giftcard;
	}
	public void setGiftcard(List<MagentoGiftcardBean> giftcard) {
		this.giftcard = giftcard;
	}
	public String getPoint_fee() {
		return point_fee;
	}
	public void setPoint_fee(String pointFee) {
		point_fee = pointFee;
	}
	public String getOther_iphost() {
		return other_iphost;
	}
	public void setOther_iphost(String otherIphost) {
		other_iphost = otherIphost;
	}
	public String getOther_totalweight() {
		return other_totalweight;
	}
	public void setOther_totalweight(String otherTotalweight) {
		other_totalweight = otherTotalweight;
	}
	public String getOther_giftmessage() {
		return other_giftmessage;
	}
	public void setOther_giftmessage(String otherGiftmessage) {
		other_giftmessage = otherGiftmessage;
	}
	public String getOther_comments() {
		return other_comments;
	}
	public String getInvoiceInfo(){
		if(StringUtils.isEmpty(other_comments)){
			return "";
		}
		int offset=other_comments.indexOf("发票类型");
		if(offset == -1){
			return "";
		}
		return other_comments.substring(offset);
	}
	public void setOther_comments(String otherComments) {
		other_comments = otherComments;
	}
	public List<MagentoOrderBean> getOrder() {
		return order;
	}
	public void setOrder(List<MagentoOrderBean> order) {
		this.order = order;
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
}
