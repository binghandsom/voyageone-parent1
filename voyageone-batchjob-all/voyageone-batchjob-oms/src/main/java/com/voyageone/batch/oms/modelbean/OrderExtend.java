package com.voyageone.batch.oms.modelbean;

import java.math.BigDecimal;
import java.util.List;

public class OrderExtend implements java.io.Serializable {
	/**
	 * 订单，订单明细信息
	 */
	private static final long serialVersionUID = -3313364598006416940L;

	/**
	 * 	订单信息
	 */
	 // 订单号
	private String orderNumber;

	 // 源订单号
	private String sourceOrderId;

	 // 原始订单号（差价订单使用）
	private String originSourceOrderId;

	// 订单日期
	private String orderDate;

	// 订单日期时间
	private String orderDateTime;

	// 客户ID
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

	// account 顾客的支付宝账号
	private String account;

	// customerComment
	private String customerComment;

	// internalMessage
	private String internalMessage;

	// giftMessage
	private String giftMessage;

	// product Total
	private String productTotal;

	// shipping Total
	private String shippingTotal;

	// grand total
	private String grandTotal;

	// shipping
	private String shipping;

	// shippingName
	private String shippingName;

	// discount
	private String discount;

	// revised discount
	private String revisedDiscount;

	// coupon
	private String coupon;

	// coupon discount
	private String couponDiscount;

	//coupon ok
	private String couponOk;

	// surcharge
	private String surcharge;

	// revised surcharge
	private String revisedSurcharge;

	// approved
	private boolean approved;

	// cancelled
	private boolean cancelled;

	// final_product_total（revised product Total）
	private String finalProductTotal;

	// revised shipping
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

	/**
	 * 店铺Id
	 */
	private String orderChannelId;

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

	private String payNo;

	/**
	 * 	订单明细信息
	 */
	//订单明细连番
	private String itemNumber;

	private boolean adjustment;

	private String product;

	private String subItemNumber;

	private String pricePerUnit;

	private String quantityOrdered;

	private String quantityShipped;

	private String quantityReturned;

	private String sku;

	private String dateShipped;

	// 订单明细状态（Code）
	private String status;

	// 订单明细状态（名称）
	private String statusName;

	// reservation 状态（Code）
	private String resStatus;

	// reservation 状态（名称）
	private String resStatusName;

	// US-Pricing (extraCurrency5)
	private String usPricing;

	// syncSynship (integer4)
	private boolean syncSynship;

	// Reservation-ID (integer5)
	private String resId;

	//  modified（更新时间）
	private String modified;

	// 产品图片路径（WS取得）
	private String imgPath;

	// 分配仓库（synship取得）
	private String storehouse;

	// 库存（WS取得）
	private String inventory;

	// 原因（删除明细时用）
	private String reason;

	// 发货运单号
	private String trackingNo;

	// syn_ship_no
	private String synShipNo;

	// 发货url
	private String synShipPath;

	// 天猫url
	private String skuTmallPath;

	// 发货方式
	private String shipChannel;

	// 发货日期
	private String shipTime;

	// 发货地区（1：国际，0：国内）
	private String trackingArea;

	// 发货类型
	private String trackingType;

	// 显示标志位
	private boolean showFlag;

	// 最终售价
	private String price;

	// 扩展字段
	// 	第三方SKU
	private String clientSku;

	// 	淘宝物流单号
	private String taobaoLogisticsId;

	// 	明细折扣
	private String itemDiscount;
	//	订单折扣
	private String orderDiscount;

	//	BCBG订单使用 start
	private String shipDate;
	private String UPC;
	private String style;
	private String color;
	private String size;
	private String lineNumber;
	private String MSRP;
	//	BCBG订单使用 end

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getFinalProductTotal() {
		return finalProductTotal;
	}

	public void setFinalProductTotal(String finalProductTotal) {
		this.finalProductTotal = finalProductTotal;
	}

	public String getFinalShippingTotal() {
		return finalShippingTotal;
	}

	public void setFinalShippingTotal(String finalShippingTotal) {
		this.finalShippingTotal = finalShippingTotal;
	}

	public String getFinalGrandTotal() {
		return finalGrandTotal;
	}

	public void setFinalGrandTotal(String finalGrandTotal) {
		this.finalGrandTotal = finalGrandTotal;
	}

	public String getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(String balanceDue) {
		this.balanceDue = balanceDue;
	}

	public String getShippedWeight() {
		return shippedWeight;
	}

	public void setShippedWeight(String shippedWeight) {
		this.shippedWeight = shippedWeight;
	}

	public String getRevisedCouponDiscount() {
		return revisedCouponDiscount;
	}

	public void setRevisedCouponDiscount(String revisedCouponDiscount) {
		this.revisedCouponDiscount = revisedCouponDiscount;
	}

	public String getActualShippedWeight() {
		return actualShippedWeight;
	}

	public void setActualShippedWeight(String actualShippedWeight) {
		this.actualShippedWeight = actualShippedWeight;
	}

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

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
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

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getWangwangId() {
		return wangwangId;
	}

	public void setWangwangId(String wangwangId) {
		this.wangwangId = wangwangId;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(String invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public String getInvoiceKind() {
		return invoiceKind;
	}

	public void setInvoiceKind(String invoiceKind) {
		this.invoiceKind = invoiceKind;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
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

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public boolean isAdjustment() {
		return adjustment;
	}

	public void setAdjustment(boolean adjustment) {
		this.adjustment = adjustment;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSubItemNumber() {
		return subItemNumber;
	}

	public void setSubItemNumber(String subItemNumber) {
		this.subItemNumber = subItemNumber;
	}

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(String quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public String getQuantityShipped() {
		return quantityShipped;
	}

	public void setQuantityShipped(String quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	public String getQuantityReturned() {
		return quantityReturned;
	}

	public void setQuantityReturned(String quantityReturned) {
		this.quantityReturned = quantityReturned;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDateShipped() {
		return dateShipped;
	}

	public void setDateShipped(String dateShipped) {
		this.dateShipped = dateShipped;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getResStatus() {
		return resStatus;
	}

	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}

	public String getResStatusName() {
		return resStatusName;
	}

	public void setResStatusName(String resStatusName) {
		this.resStatusName = resStatusName;
	}

	public String getUsPricing() {
		return usPricing;
	}

	public void setUsPricing(String usPricing) {
		this.usPricing = usPricing;
	}

	public boolean isSyncSynship() {
		return syncSynship;
	}

	public void setSyncSynship(boolean syncSynship) {
		this.syncSynship = syncSynship;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getStorehouse() {
		return storehouse;
	}

	public void setStorehouse(String storehouse) {
		this.storehouse = storehouse;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public String getSynShipNo() {
		return synShipNo;
	}

	public void setSynShipNo(String synShipNo) {
		this.synShipNo = synShipNo;
	}

	public String getSynShipPath() {
		return synShipPath;
	}

	public void setSynShipPath(String synShipPath) {
		this.synShipPath = synShipPath;
	}

	public String getSkuTmallPath() {
		return skuTmallPath;
	}

	public void setSkuTmallPath(String skuTmallPath) {
		this.skuTmallPath = skuTmallPath;
	}

	public String getShipChannel() {
		return shipChannel;
	}

	public void setShipChannel(String shipChannel) {
		this.shipChannel = shipChannel;
	}

	public String getShipTime() {
		return shipTime;
	}

	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}

	public String getTrackingArea() {
		return trackingArea;
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getTrackingType() {
		return trackingType;
	}

	public void setTrackingType(String trackingType) {
		this.trackingType = trackingType;
	}

	public boolean isShowFlag() {
		return showFlag;
	}

	public void setShowFlag(boolean showFlag) {
		this.showFlag = showFlag;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getClientSku() {
		return clientSku;
	}

	public void setClientSku(String clientSku) {
		this.clientSku = clientSku;
	}

	public String getTaobaoLogisticsId() {
		return taobaoLogisticsId;
	}

	public void setTaobaoLogisticsId(String taobaoLogisticsId) {
		this.taobaoLogisticsId = taobaoLogisticsId;
	}

	public String getItemDiscount() {
		return itemDiscount;
	}

	public void setItemDiscount(String itemDiscount) {
		this.itemDiscount = itemDiscount;
	}

	public String getOrderDiscount() {
		return orderDiscount;
	}

	public void setOrderDiscount(String orderDiscount) {
		this.orderDiscount = orderDiscount;
	}

	public String getUPC() {
		return UPC;
	}

	public void setUPC(String UPC) {
		this.UPC = UPC;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getMSRP() {
		return MSRP;
	}

	public void setMSRP(String MSRP) {
		this.MSRP = MSRP;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
}