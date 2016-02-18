package com.voyageone.batch.cms.formbean;

import java.util.List;

public class OutFormOrderdetailOrders {

	/**
	 * 订单号
	 */
	private String orderNumber;
	
	/**
	 * 源订单号
	 */
	private String sourceOrderId;
	
	/**
	 * 原始订单号（差价订单使用）
	 */
	private String originSourceOrderId;
	
	/**
	 * 订单日期
	 */
	private String orderDate;
	
	/**
	 * 订单时间
	 */
	private String orderTime;
	
	/**
	 * 订单日期时间
	 */
	private String orderDateTime;
	
	/**
	 * 客户ID
	 */
	private String customerId;
	
	/**
	 * Sold To
	 */
	private String name;
	private String company;
	private String email;
	private String address;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String phone;
	
	/**
	 * Ship To
	 */
	private String shipName;
	private String shipCompany;
	private String shipEmail;
	private String shipAddress;
	private String shipAddress2;
	private String shipCity;
	private String shipState;
	private String shipZip;
	private String shipCountry;
	private String shipPhone;
	
	/**
	 * account 顾客的支付宝账号
	 */
	private String account;
	
	/**
	 * customerComment
	 */	
	private String customerComment;
	
	/**
	 * internalMessage
	 */
	private String internalMessage;

	/**
	 * giftMessage
	 */
	private String giftMessage;
	/**
	 * product Total
	 */
	private String productTotal;
	
	/**
	 * shipping Total
	 */
	private String shippingTotal;
	
	/**
	 * grand total
	 */
	private String grandTotal;
	
	/**
	 * shipping
	 */
	private String shipping;
	
	/**
	 * shippingName
	 */
	private String shippingName;
	
	/**
	 * discount
	 */
	private String discount;
	
	/**
	 * revised discount（全体）
	 */
	private String revisedDiscount;

	/**
	 * revised discount（订单折扣）
	 */
	private String revisedOrderDiscount;
	
	/**
	 * coupon
	 */
	private String coupon;
	
	/**
	 * coupon discount
	 */
	private String couponDiscount;
	
	/**
	 * coupon ok
	 */
	private String couponOk;
	
	/**
	 * surcharge
	 */
	private String surcharge;
	
	/**
	 * revised surcharge
	 */
	private String revisedSurcharge;
	
	/**
	 * approved
	 */
	private boolean approved;
	
	/**
	 * cancelled
	 */
	private boolean cancelled;
	
	/**
	 * final_product_total（revised product Total）
	 */
	private String finalProductTotal;
	
	/**
	 * revised shipping
	 */
	private String finalShippingTotal;
	
	/**
	 * revised grand total
	 */
	private String finalGrandTotal;
	
	/**
	 * balancedue
	 */
	private String balanceDue;
	
	/**
	 * shipped_weight
	 */
	private String shippedWeight;
	
	/**
	 * revised_coupon_discount
	 */
	private String revisedCouponDiscount;
	
	/**
	 * actual_shipped_weight
	 */
	private String actualShippedWeight;
	
	/**
	 * local_sort_text1（local ship on hold:lock）
	 * 锁单，仅与前端交互用
	 */
	private boolean localShipOnHold;
	
	/**
	 * 锁单与DB保持一致，用文言
	 */
	private String lockShip;
	
	/**
	 * local_sort_integer1（wait real refund）
	 */
	private boolean waitRealRefund;
	
	/**
	 * local_sort_integer2（price difference no pay）
	 */
	private boolean priceDifferenceNoPay;
	
	/**
	 * localSortCurrency5（Use Tmall Point Fee）
	 */
	private String useTmallPointFee;
	
	/**
	 * 订单状态
	 */
	private String orderStatus;
	
	/**
	 * 订单状态名称
	 */
	private String orderStatusName;
	
	/**
	 * 渠道Id
	 */
	private String cartId;
	
	/**
	 * 渠道名
	 */
	private String cartName;
	
//	/**
//	 * 渠道名
//	 */
//	private String channel;
	
	/**
	 * approval_date(含时间)
	 */
	private String approvalDate;
	
	/**
	 * po_number
	 */
	private String poNumber;
	
//	/**
//	 * expected_net
//	 */
//	private String expectedNet;
//	
//	/**
//	 * actual_net
//	 */
//	private String actualNet;
	
	/**
	 * 店铺Id
	 */
	private String orderChannelId;
	
//	/**
//	 * 店铺名
//	 */
//	private String store;
	
	/**
	 * 
	 */
	private String wangwangId;
	
	/**
	 * local_sort_text2（invoice）
	 */
	private String invoice;
	
	/**
	 * 
	 */
	private String invoiceInfo;
	
	/**
	 * 		invoice_kind
	 */
	private String invoiceKind;

	/**
	 * 货币种类（附加字段：$,¥）
	 */
	private String currencyType;
	
	/**
	 * 付款信息标题（附加字段）
	 */
	private String payTitleText;
	
	/**
	 * 付款信息值（附加字段）
	 */
	private String payTitleValue;
	
	/**
	 * 打折类型
	 */
	private String discountType;
	
	/**
	 * 打折百分比
	 */
	private String discountPercent;
	
	/**
	 * 订单类型
	 */
	private String orderKind;
	
	/**
	 * 源订单号（连番）
	 */
	private String subSourceOrderId;
	
	//	修正前信息缓存（Note输出用）
	//	Comment
	/**
	 * 		Customer Message
	 */
	private String origCustomerComment;
	
	/**
	 * 		internalMessage
	 */
	private String origInternalMessage;

	/**
	 * 		GiftMessage
	 */
	private String origGiftMessage;
	
	/**
	 * 		local_sort_text2（invoice）
	 */
	private String origInvoice;
	
	/**
	 * 		invoice_info
	 */
	private String origInvoiceInfo;
	
	/**
	 * 		invoice_kind
	 */
	private String origInvoiceKind;
	
	//	Shipping
	/**
	 * 		shipping
	 */
	private String origShipping;
	/**
	 * 		shipping_name
	 */
	private String origShippingName;
	
	//	Address
	/**
	 * 		Sold To
	 */
	private String origName;
	private String origCompany;
	private String origEmail;
	private String origAddress;
	private String origAddress2;
	private String origCity;
	private String origState;
	private String origZip;
	private String origCountry;
	private String origPhone;
	
	/**
	 * 		Ship To
	 */
	private String origShipName;
	private String origShipCompany;
	private String origShipEmail;
	private String origShipAddress;
	private String origShipAddress2;
	private String origShipCity;
	private String origShipState;
	private String origShipZip;
	private String origShipCountry;
	private String origShipPhone;

	// synship 用
	/**
	 * 		ship_district
	 */
	private String synShipShipDistrict;
	
	/**
	 * 		ship_address
	 */
	private String synShipShipAddress;
		
	/**
	 * 
	 */
	private String creater;

	/**
	 * 
	 */
	private String modifier;
	
	/**
	 * 差价订单标志（ 0 正常， 1 差价）
	 */
	private boolean priceDifferenceFlag;
	
	/**
	 * oms_bt_group_orders 专用
	 */
	/**
	 *		expected
	 */
	private String expected;
	
	/**
	 *		paymentTotal
	 */
	private String paymentTotal;
	
	/**
	 *		refundTotal
	 */
	private String refundTotal;
	
	/**
	 *		orderChannelName（SN）
	 */
	private String orderChannelName;
	
	/**
	 *		debitTotal（transaction 关联，程序集计输出）
	 */
	private String debitTotal;
	
	/**
	 *		creditTotal（transaction 关联，程序集计输出）
	 */
	private String creditTotal;
	
	/**
	 *		customerRefund
	 */
	private String customerRefund;
	
	/**
	 *		运输渠道
	 */
	private String shipChannel;	
	
	/**
	 *		第三方订单号
	 */
	private String clientOrderId;
	
	/**
	 *		是否运费到付
	 */
	private boolean freightCollect;
	
	/**
	 *		原始是否运费到付
	 */
	private boolean origFreightCollect;

	/**
	 *		原始存在退款历史信息
	 */
	private boolean isHaveRefundHistory;

	/**
	 *		平台ID
	 */
	private String platformId;

	/**
	 *		第三方订单取消标志
	 */
	private boolean cancelClientOrderFlag;

	/**
	 *		第三方订单取消标志发送标志
	 */
	private boolean cancelClientOrderSendFlag;

	/**
	 *		原始第三方订单取消标志
	 */
	private boolean origCancelClientOrderFlag;

	/**
	 *		第三方订单推送标志
	 */
	private boolean clientOrderSendFlag;

	/**
	 *		第三方取消订单标志
	 */
	private boolean thirdPartyCancelOrderFlag;

	/**
	 *		淘宝物流单号（菜鸟物流单号）
	 */
	private String taobaoLogisticsId;

	/**
	 *		扩展文本
	 */
	private String extTxt1;
	
	/**
	 * 订单明细
	 */
	private List<OutFormOrderDetailOrderDetail> orderDetailsList;

	
	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the sourceOrderId
	 */
	public String getSourceOrderId() {
		return sourceOrderId;
	}

	/**
	 * @param sourceOrderId the sourceOrderId to set
	 */
	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public String getOriginSourceOrderId() {
		return originSourceOrderId;
	}

	public void setOriginSourceOrderId(String originSourceOrderId) {
		this.originSourceOrderId = originSourceOrderId;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the finalGrandTotal
	 */
	public String getFinalGrandTotal() {
		return finalGrandTotal;
	}

	/**
	 * @param finalGrandTotal the finalGrandTotal to set
	 */
	public void setFinalGrandTotal(String finalGrandTotal) {
		this.finalGrandTotal = finalGrandTotal;
	}

	/**
	 * @return the balanceDue
	 */
	public String getBalanceDue() {
		return balanceDue;
	}

	/**
	 * @param balanceDue the balanceDue to set
	 */
	public void setBalanceDue(String balanceDue) {
		this.balanceDue = balanceDue;
	}

	public String getShippedWeight() {
		return shippedWeight;
	}

	public void setShippedWeight(String shippedWeight) {
		this.shippedWeight = shippedWeight;
	}

	public String getActualShippedWeight() {
		return actualShippedWeight;
	}

	public void setActualShippedWeight(String actualShippedWeight) {
		this.actualShippedWeight = actualShippedWeight;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	/**
	 * @return the wangwangId
	 */
	public String getWangwangId() {
		return wangwangId;
	}

	/**
	 * @param wangwangId the wangwangId to set
	 */
	public void setWangwangId(String wangwangId) {
		this.wangwangId = wangwangId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShipCompany() {
		return shipCompany;
	}

	public void setShipCompany(String shipCompany) {
		this.shipCompany = shipCompany;
	}

	public String getShipEmail() {
		return shipEmail;
	}

	public void setShipEmail(String shipEmail) {
		this.shipEmail = shipEmail;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getShipAddress2() {
		return shipAddress2;
	}

	public void setShipAddress2(String shipAddress2) {
		this.shipAddress2 = shipAddress2;
	}

	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipState() {
		return shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public String getShipZip() {
		return shipZip;
	}

	public void setShipZip(String shipZip) {
		this.shipZip = shipZip;
	}

	public String getShipCountry() {
		return shipCountry;
	}

	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}

	public String getShipPhone() {
		return shipPhone;
	}

	public void setShipPhone(String shipPhone) {
		this.shipPhone = shipPhone;
	}

	public String getCustomerComment() {
		return customerComment;
	}

	public void setCustomerComment(String customerComment) {
		this.customerComment = customerComment;
	}

	public String getInternalMessage() {
		return internalMessage;
	}

	public void setInternalMessage(String internalMessage) {
		this.internalMessage = internalMessage;
	}

	public String getGiftMessage() {
		return giftMessage;
	}

	public void setGiftMessage(String giftMessage) {
		this.giftMessage = giftMessage;
	}

	public String getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(String productTotal) {
		this.productTotal = productTotal;
	}

	public String getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(String shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public String getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}	
	
	public String getShipping() {
		return shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getRevisedDiscount() {
		return revisedDiscount;
	}

	public void setRevisedDiscount(String revisedDiscount) {
		this.revisedDiscount = revisedDiscount;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(String couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public String getCouponOk() {
		return couponOk;
	}

	public void setCouponOk(String couponOk) {
		this.couponOk = couponOk;
	}

	public String getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(String surcharge) {
		this.surcharge = surcharge;
	}

	public String getRevisedSurcharge() {
		return revisedSurcharge;
	}

	public void setRevisedSurcharge(String revisedSurcharge) {
		this.revisedSurcharge = revisedSurcharge;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getFinalShippingTotal() {
		return finalShippingTotal;
	}

	public void setFinalShippingTotal(String finalShippingTotal) {
		this.finalShippingTotal = finalShippingTotal;
	}	

	public boolean isWaitRealRefund() {
		return waitRealRefund;
	}

	public void setWaitRealRefund(boolean waitRealRefund) {
		this.waitRealRefund = waitRealRefund;
	}

	public boolean isPriceDifferenceNoPay() {
		return priceDifferenceNoPay;
	}

	public void setPriceDifferenceNoPay(boolean priceDifferenceNoPay) {
		this.priceDifferenceNoPay = priceDifferenceNoPay;
	}

	public String getUseTmallPointFee() {
		return useTmallPointFee;
	}

	public void setUseTmallPointFee(String useTmallPointFee) {
		this.useTmallPointFee = useTmallPointFee;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getCartName() {
		return cartName;
	}

	public void setCartName(String cartName) {
		this.cartName = cartName;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

//	public String getExpectedNet() {
//		return expectedNet;
//	}
//
//	public void setExpectedNet(String expectedNet) {
//		this.expectedNet = expectedNet;
//	}
//
//	public String getActualNet() {
//		return actualNet;
//	}
//
//	public void setActualNet(String actualNet) {
//		this.actualNet = actualNet;
//	}

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

//	public String getChannel() {
//		return channel;
//	}
//
//	public void setChannel(String channel) {
//		this.channel = channel;
//	}

//	public String getStore() {
//		return store;
//	}
//
//	public void setStore(String store) {
//		this.store = store;
//	}

	public boolean isLocalShipOnHold() {
		return localShipOnHold;
	}

	public void setLocalShipOnHold(boolean localShipOnHold) {
		this.localShipOnHold = localShipOnHold;
	}

	public String getLockShip() {
		return lockShip;
	}

	public void setLockShip(String lockShip) {
		this.lockShip = lockShip;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(String invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getFinalProductTotal() {
		return finalProductTotal;
	}

	public void setFinalProductTotal(String finalProductTotal) {
		this.finalProductTotal = finalProductTotal;
	}

	public String getPayTitleText() {
		return payTitleText;
	}

	public void setPayTitleText(String payTitleText) {
		this.payTitleText = payTitleText;
	}

	public String getPayTitleValue() {
		return payTitleValue;
	}

	public void setPayTitleValue(String payTitleValue) {
		this.payTitleValue = payTitleValue;
	}

	public String getRevisedCouponDiscount() {
		return revisedCouponDiscount;
	}

	public void setRevisedCouponDiscount(String revisedCouponDiscount) {
		this.revisedCouponDiscount = revisedCouponDiscount;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getOrderKind() {
		return orderKind;
	}

	public void setOrderKind(String orderKind) {
		this.orderKind = orderKind;
	}

	public String getSubSourceOrderId() {
		return subSourceOrderId;
	}

	public void setSubSourceOrderId(String subSourceOrderId) {
		this.subSourceOrderId = subSourceOrderId;
	}

	public String getOrigCustomerComment() {
		return origCustomerComment;
	}

	public void setOrigCustomerComment(String origCustomerComment) {
		this.origCustomerComment = origCustomerComment;
	}

	public String getOrigInternalMessage() {
		return origInternalMessage;
	}

	public void setOrigInternalMessage(String origInternalMessage) {
		this.origInternalMessage = origInternalMessage;
	}

	public String getOrigGiftMessage() {
		return origGiftMessage;
	}

	public void setOrigGiftMessage(String origGiftMessage) {
		this.origGiftMessage = origGiftMessage;
	}

	public String getOrigInvoice() {
		return origInvoice;
	}

	public void setOrigInvoice(String origInvoice) {
		this.origInvoice = origInvoice;
	}

	public String getOrigInvoiceInfo() {
		return origInvoiceInfo;
	}

	public void setOrigInvoiceInfo(String origInvoiceInfo) {
		this.origInvoiceInfo = origInvoiceInfo;
	}

	public String getOrigShipping() {
		return origShipping;
	}

	public void setOrigShipping(String origShipping) {
		this.origShipping = origShipping;
	}

	public String getOrigShippingName() {
		return origShippingName;
	}

	public void setOrigShippingName(String origShippingName) {
		this.origShippingName = origShippingName;
	}

	public String getOrigName() {
		return origName;
	}

	public void setOrigName(String origName) {
		this.origName = origName;
	}

	public String getOrigCompany() {
		return origCompany;
	}

	public void setOrigCompany(String origCompany) {
		this.origCompany = origCompany;
	}

	public String getOrigEmail() {
		return origEmail;
	}

	public void setOrigEmail(String origEmail) {
		this.origEmail = origEmail;
	}

	public String getOrigAddress() {
		return origAddress;
	}

	public void setOrigAddress(String origAddress) {
		this.origAddress = origAddress;
	}

	public String getOrigAddress2() {
		return origAddress2;
	}

	public void setOrigAddress2(String origAddress2) {
		this.origAddress2 = origAddress2;
	}

	public String getOrigCity() {
		return origCity;
	}

	public void setOrigCity(String origCity) {
		this.origCity = origCity;
	}

	public String getOrigState() {
		return origState;
	}

	public void setOrigState(String origState) {
		this.origState = origState;
	}

	public String getOrigZip() {
		return origZip;
	}

	public void setOrigZip(String origZip) {
		this.origZip = origZip;
	}

	public String getOrigCountry() {
		return origCountry;
	}

	public void setOrigCountry(String origCountry) {
		this.origCountry = origCountry;
	}

	public String getOrigPhone() {
		return origPhone;
	}

	public void setOrigPhone(String origPhone) {
		this.origPhone = origPhone;
	}

	public String getOrigShipName() {
		return origShipName;
	}

	public void setOrigShipName(String origShipName) {
		this.origShipName = origShipName;
	}

	public String getOrigShipCompany() {
		return origShipCompany;
	}

	public void setOrigShipCompany(String origShipCompany) {
		this.origShipCompany = origShipCompany;
	}

	public String getOrigShipEmail() {
		return origShipEmail;
	}

	public void setOrigShipEmail(String origShipEmail) {
		this.origShipEmail = origShipEmail;
	}

	public String getOrigShipAddress() {
		return origShipAddress;
	}

	public void setOrigShipAddress(String origShipAddress) {
		this.origShipAddress = origShipAddress;
	}

	public String getOrigShipAddress2() {
		return origShipAddress2;
	}

	public void setOrigShipAddress2(String origShipAddress2) {
		this.origShipAddress2 = origShipAddress2;
	}

	public String getOrigShipCity() {
		return origShipCity;
	}

	public void setOrigShipCity(String origShipCity) {
		this.origShipCity = origShipCity;
	}

	public String getOrigShipState() {
		return origShipState;
	}

	public void setOrigShipState(String origShipState) {
		this.origShipState = origShipState;
	}

	public String getOrigShipZip() {
		return origShipZip;
	}

	public void setOrigShipZip(String origShipZip) {
		this.origShipZip = origShipZip;
	}

	public String getOrigShipCountry() {
		return origShipCountry;
	}

	public void setOrigShipCountry(String origShipCountry) {
		this.origShipCountry = origShipCountry;
	}

	public String getOrigShipPhone() {
		return origShipPhone;
	}

	public void setOrigShipPhone(String origShipPhone) {
		this.origShipPhone = origShipPhone;
	}

	public String getSynShipShipDistrict() {
		return synShipShipDistrict;
	}

	public void setSynShipShipDistrict(String synShipShipDistrict) {
		this.synShipShipDistrict = synShipShipDistrict;
	}

	public String getSynShipShipAddress() {
		return synShipShipAddress;
	}

	public void setSynShipShipAddress(String synShipShipAddress) {
		this.synShipShipAddress = synShipShipAddress;
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

	public boolean isPriceDifferenceFlag() {
		return priceDifferenceFlag;
	}

	public void setPriceDifferenceFlag(boolean priceDifferenceFlag) {
		this.priceDifferenceFlag = priceDifferenceFlag;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(String paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public String getRefundTotal() {
		return refundTotal;
	}

	public void setRefundTotal(String refundTotal) {
		this.refundTotal = refundTotal;
	}

	public String getOrderChannelName() {
		return orderChannelName;
	}

	public void setOrderChannelName(String orderChannelName) {
		this.orderChannelName = orderChannelName;
	}
	
	public String getDebitTotal() {
		return debitTotal;
	}

	public void setDebitTotal(String debitTotal) {
		this.debitTotal = debitTotal;
	}

	public String getCreditTotal() {
		return creditTotal;
	}

	public void setCreditTotal(String creditTotal) {
		this.creditTotal = creditTotal;
	}

	public String getCustomerRefund() {
		return customerRefund;
	}

	public void setCustomerRefund(String customerRefund) {
		this.customerRefund = customerRefund;
	}

	public String getShipChannel() {
		return shipChannel;
	}

	public void setShipChannel(String shipChannel) {
		this.shipChannel = shipChannel;
	}

	public String getInvoiceKind() {
		return invoiceKind;
	}

	public void setInvoiceKind(String invoiceKind) {
		this.invoiceKind = invoiceKind;
	}

	public String getOrigInvoiceKind() {
		return origInvoiceKind;
	}

	public void setOrigInvoiceKind(String origInvoiceKind) {
		this.origInvoiceKind = origInvoiceKind;
	}
	
	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	public boolean isFreightCollect() {
		return freightCollect;
	}

	public void setFreightCollect(boolean freightCollect) {
		this.freightCollect = freightCollect;
	}

	public boolean isOrigFreightCollect() {
		return origFreightCollect;
	}

	public void setOrigFreightCollect(boolean origFreightCollect) {
		this.origFreightCollect = origFreightCollect;
	}

	public boolean isHaveRefundHistory() {
		return isHaveRefundHistory;
	}

	public void setIsHaveRefundHistory(boolean isHaveRefundHistory) {
		this.isHaveRefundHistory = isHaveRefundHistory;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public boolean isCancelClientOrderFlag() {
		return cancelClientOrderFlag;
	}

	public void setCancelClientOrderFlag(boolean cancelClientOrderFlag) {
		this.cancelClientOrderFlag = cancelClientOrderFlag;
	}

	public boolean isOrigCancelClientOrderFlag() {
		return origCancelClientOrderFlag;
	}

	public void setOrigCancelClientOrderFlag(boolean origCancelClientOrderFlag) {
		this.origCancelClientOrderFlag = origCancelClientOrderFlag;
	}

	public boolean isCancelClientOrderSendFlag() {
		return cancelClientOrderSendFlag;
	}

	public void setCancelClientOrderSendFlag(boolean cancelClientOrderSendFlag) {
		this.cancelClientOrderSendFlag = cancelClientOrderSendFlag;
	}

	public boolean isClientOrderSendFlag() {
		return clientOrderSendFlag;
	}

	public void setClientOrderSendFlag(boolean clientOrderSendFlag) {
		this.clientOrderSendFlag = clientOrderSendFlag;
	}

	public boolean isThirdPartyCancelOrderFlag() {
		return thirdPartyCancelOrderFlag;
	}

	public void setThirdPartyCancelOrderFlag(boolean thirdPartyCancelOrderFlag) {
		this.thirdPartyCancelOrderFlag = thirdPartyCancelOrderFlag;
	}

	public String getRevisedOrderDiscount() {
		return revisedOrderDiscount;
	}

	public void setRevisedOrderDiscount(String revisedOrderDiscount) {
		this.revisedOrderDiscount = revisedOrderDiscount;
	}

	public String getTaobaoLogisticsId() {
		return taobaoLogisticsId;
	}

	public void setTaobaoLogisticsId(String taobaoLogisticsId) {
		this.taobaoLogisticsId = taobaoLogisticsId;
	}

	public String getExtTxt1() {
		return extTxt1;
	}

	public void setExtTxt1(String extTxt1) {
		this.extTxt1 = extTxt1;
	}

	public List<OutFormOrderDetailOrderDetail> getOrderDetailsList() {
		return orderDetailsList;
	}

	public void setOrderDetailsList(
			List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}
}
