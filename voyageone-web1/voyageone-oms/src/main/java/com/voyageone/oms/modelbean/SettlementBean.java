package com.voyageone.oms.modelbean;


public class SettlementBean {

	//	订单渠道ID
	private String orderChannelId;
	//
	private String cartId;
	//	文件类型（1：settlement，2：transaction）
	private String fileType;
	//	付款日
	private String paymentTime;
	//	结算日
	private String settlementTime;
	//	网络订单号
	private String sourceOrderId;
	//	原始网络订单号
	private String originSourceOrderId;
	//	业务类型
	private String businessType;
	//	备注
	private String comment;
	//	账务方式
	private String payType;
	//	收入（人民币（物品金额，+ 收入 - 支出（退货）））
	private String debit;
	//	收入外币
	private String debitForeign;
	//	支出（人民币（费用佣金，+ 支出 - 收入））
	private String credit;
	//	支出外币
	private String creditForeign;
	//	汇率
	private String rate;
	//	外币币种
	private String currency;
	//  手续费
	private String fee;
	//	费率（目前微信使用）
	private String feeRate;
	//	交易号
	private String payNo;
	//	支付账号
	private String payAccount;
	//
	private String description;
	//	上传文件ID
	private String settlementFileId;
	//	账务方式明细
	private String pay_type_detail;
	//	结算额
	private String settlement;
	//	结算额外币
	private String settlement_foreign;
	//	处理标志
	private Boolean process_flag;
	//
	private String trade_type;
	//
	private String trade_status;
	//
	private String ali_int_stem_from;
	//	公众账号ID
	private String wx_public_account_id;
	//	商户号
	private String wx_shop_id;
	//	特约商户号
	private String wx_special_shop_id;
	//	设备号
	private String wx_device_id;
	//	微信订单号
	private String wx_weixin_order_id;
	//	商户订单号
	private String shop_order_id;
	//	用户标识
	private String wx_user_id;
	//	付款银行
	private String payment_channel;
	//	企业红包金额
	private String wx_company_bonus;
	//	微信退款单号
	private String wx_weixin_refund_id;
	//	商户退款单号
	private String wx_shop_refund_id;
	//	企业红包退款金额
	private String wx_company_bonus_refund;
	//	退款类型
	private String wx_refund_type;
	//	退款状态
	private String wx_refund_status;
	//	商户数据包
	private String wx_shop_data_package;
	//	商品名称
	private String goods_name;
	//	账务流水号
	private String ali_accounting_no;
	//	对方账号
	private String ali_pay_account_full;
	//	账户余额（元）
	private String ali_account_balance;
	//	下单时间
	private String jg_order_time;
	//	货款
	private String jg_product_total;
	//	代收配送费
	private String jg_shipping_total;
	//	订单总金额
	private String jg_order_total;
	//	商家促销金额
	private String jg_sales_total;
	//	满减优惠金额
	private String jg_more_off_sales_total;
	//	店铺京券金额
	private String jg_coupon_jing_total;
	//	店铺东券金额
	private String jg_coupon_dong_total;
	//	退货时间
	private String jg_refund_time;
	//	返修单号
	private String jg_refund_id;
	//	一级类目
	private String jg_class1_category;
	//	二级类目
	private String jg_class2_category;
	//	三级类目
	private String jg_class3_category;
	//	SKU编号
	private String jg_sku_no;
	//	货号
	private String jg_goods_no;
	//	SKU单价
	private String jg_sku_price_unit;
	//	单据编号
	private String jg_ticket_no;
	//	费用类型
	private String jg_fee_type;
	//	调账类型
	private String jg_adjustment_type;

	private String creater;
	private String modifier;


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

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getSettlementTime() {
		return settlementTime;
	}

	public void setSettlementTime(String settlementTime) {
		this.settlementTime = settlementTime;
	}

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

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public String getDebitForeign() {
		return debitForeign;
	}

	public void setDebitForeign(String debitForeign) {
		this.debitForeign = debitForeign;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getCreditForeign() {
		return creditForeign;
	}

	public void setCreditForeign(String creditForeign) {
		this.creditForeign = creditForeign;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSettlementFileId() {
		return settlementFileId;
	}

	public void setSettlementFileId(String settlementFileId) {
		this.settlementFileId = settlementFileId;
	}

	public String getPay_type_detail() {
		return pay_type_detail;
	}

	public void setPay_type_detail(String pay_type_detail) {
		this.pay_type_detail = pay_type_detail;
	}

	public String getSettlement() {
		return settlement;
	}

	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}

	public String getSettlement_foreign() {
		return settlement_foreign;
	}

	public void setSettlement_foreign(String settlement_foreign) {
		this.settlement_foreign = settlement_foreign;
	}

	public Boolean isProcess_flag() {
		return process_flag;
	}

	public void setProcess_flag(Boolean process_flag) {
		this.process_flag = process_flag;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getAli_int_stem_from() {
		return ali_int_stem_from;
	}

	public void setAli_int_stem_from(String ali_int_stem_from) {
		this.ali_int_stem_from = ali_int_stem_from;
	}

	public String getWx_public_account_id() {
		return wx_public_account_id;
	}

	public void setWx_public_account_id(String wx_public_account_id) {
		this.wx_public_account_id = wx_public_account_id;
	}

	public String getWx_shop_id() {
		return wx_shop_id;
	}

	public void setWx_shop_id(String wx_shop_id) {
		this.wx_shop_id = wx_shop_id;
	}

	public String getWx_special_shop_id() {
		return wx_special_shop_id;
	}

	public void setWx_special_shop_id(String wx_special_shop_id) {
		this.wx_special_shop_id = wx_special_shop_id;
	}

	public String getWx_device_id() {
		return wx_device_id;
	}

	public void setWx_device_id(String wx_device_id) {
		this.wx_device_id = wx_device_id;
	}

	public String getWx_weixin_order_id() {
		return wx_weixin_order_id;
	}

	public void setWx_weixin_order_id(String wx_weixin_order_id) {
		this.wx_weixin_order_id = wx_weixin_order_id;
	}

	public String getShop_order_id() {
		return shop_order_id;
	}

	public void setShop_order_id(String shop_order_id) {
		this.shop_order_id = shop_order_id;
	}

	public String getWx_user_id() {
		return wx_user_id;
	}

	public void setWx_user_id(String wx_user_id) {
		this.wx_user_id = wx_user_id;
	}

	public String getPayment_channel() {
		return payment_channel;
	}

	public void setPayment_channel(String payment_channel) {
		this.payment_channel = payment_channel;
	}

	public String getWx_company_bonus() {
		return wx_company_bonus;
	}

	public void setWx_company_bonus(String wx_company_bonus) {
		this.wx_company_bonus = wx_company_bonus;
	}

	public String getWx_weixin_refund_id() {
		return wx_weixin_refund_id;
	}

	public void setWx_weixin_refund_id(String wx_weixin_refund_id) {
		this.wx_weixin_refund_id = wx_weixin_refund_id;
	}

	public String getWx_shop_refund_id() {
		return wx_shop_refund_id;
	}

	public void setWx_shop_refund_id(String wx_shop_refund_id) {
		this.wx_shop_refund_id = wx_shop_refund_id;
	}

	public String getWx_company_bonus_refund() {
		return wx_company_bonus_refund;
	}

	public void setWx_company_bonus_refund(String wx_company_bonus_refund) {
		this.wx_company_bonus_refund = wx_company_bonus_refund;
	}

	public String getWx_refund_type() {
		return wx_refund_type;
	}

	public void setWx_refund_type(String wx_refund_type) {
		this.wx_refund_type = wx_refund_type;
	}

	public String getWx_refund_status() {
		return wx_refund_status;
	}

	public void setWx_refund_status(String wx_refund_status) {
		this.wx_refund_status = wx_refund_status;
	}

	public String getWx_shop_data_package() {
		return wx_shop_data_package;
	}

	public void setWx_shop_data_package(String wx_shop_data_package) {
		this.wx_shop_data_package = wx_shop_data_package;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getAli_accounting_no() {
		return ali_accounting_no;
	}

	public void setAli_accounting_no(String ali_accounting_no) {
		this.ali_accounting_no = ali_accounting_no;
	}

	public String getAli_pay_account_full() {
		return ali_pay_account_full;
	}

	public void setAli_pay_account_full(String ali_pay_account_full) {
		this.ali_pay_account_full = ali_pay_account_full;
	}

	public String getAli_account_balance() {
		return ali_account_balance;
	}

	public void setAli_account_balance(String ali_account_balance) {
		this.ali_account_balance = ali_account_balance;
	}

	public String getJg_order_time() {
		return jg_order_time;
	}

	public void setJg_order_time(String jg_order_time) {
		this.jg_order_time = jg_order_time;
	}

	public String getJg_product_total() {
		return jg_product_total;
	}

	public void setJg_product_total(String jg_product_total) {
		this.jg_product_total = jg_product_total;
	}

	public String getJg_shipping_total() {
		return jg_shipping_total;
	}

	public void setJg_shipping_total(String jg_shipping_total) {
		this.jg_shipping_total = jg_shipping_total;
	}

	public String getJg_order_total() {
		return jg_order_total;
	}

	public void setJg_order_total(String jg_order_total) {
		this.jg_order_total = jg_order_total;
	}

	public String getJg_sales_total() {
		return jg_sales_total;
	}

	public void setJg_sales_total(String jg_sales_total) {
		this.jg_sales_total = jg_sales_total;
	}

	public String getJg_more_off_sales_total() {
		return jg_more_off_sales_total;
	}

	public void setJg_more_off_sales_total(String jg_more_off_sales_total) {
		this.jg_more_off_sales_total = jg_more_off_sales_total;
	}

	public String getJg_coupon_jing_total() {
		return jg_coupon_jing_total;
	}

	public void setJg_coupon_jing_total(String jg_coupon_jing_total) {
		this.jg_coupon_jing_total = jg_coupon_jing_total;
	}

	public String getJg_coupon_dong_total() {
		return jg_coupon_dong_total;
	}

	public void setJg_coupon_dong_total(String jg_coupon_dong_total) {
		this.jg_coupon_dong_total = jg_coupon_dong_total;
	}

	public String getJg_refund_time() {
		return jg_refund_time;
	}

	public void setJg_refund_time(String jg_refund_time) {
		this.jg_refund_time = jg_refund_time;
	}

	public String getJg_refund_id() {
		return jg_refund_id;
	}

	public void setJg_refund_id(String jg_refund_id) {
		this.jg_refund_id = jg_refund_id;
	}

	public String getJg_class1_category() {
		return jg_class1_category;
	}

	public void setJg_class1_category(String jg_class1_category) {
		this.jg_class1_category = jg_class1_category;
	}

	public String getJg_class2_category() {
		return jg_class2_category;
	}

	public void setJg_class2_category(String jg_class2_category) {
		this.jg_class2_category = jg_class2_category;
	}

	public String getJg_class3_category() {
		return jg_class3_category;
	}

	public void setJg_class3_category(String jg_class3_category) {
		this.jg_class3_category = jg_class3_category;
	}

	public String getJg_sku_no() {
		return jg_sku_no;
	}

	public void setJg_sku_no(String jg_sku_no) {
		this.jg_sku_no = jg_sku_no;
	}

	public String getJg_goods_no() {
		return jg_goods_no;
	}

	public void setJg_goods_no(String jg_goods_no) {
		this.jg_goods_no = jg_goods_no;
	}

	public String getJg_sku_price_unit() {
		return jg_sku_price_unit;
	}

	public void setJg_sku_price_unit(String jg_sku_price_unit) {
		this.jg_sku_price_unit = jg_sku_price_unit;
	}

	public String getJg_ticket_no() {
		return jg_ticket_no;
	}

	public void setJg_ticket_no(String jg_ticket_no) {
		this.jg_ticket_no = jg_ticket_no;
	}

	public String getJg_fee_type() {
		return jg_fee_type;
	}

	public void setJg_fee_type(String jg_fee_type) {
		this.jg_fee_type = jg_fee_type;
	}

	public String getJg_adjustment_type() {
		return jg_adjustment_type;
	}

	public void setJg_adjustment_type(String jg_adjustment_type) {
		this.jg_adjustment_type = jg_adjustment_type;
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
