package com.voyageone.oms.formbean;

import java.util.List;

import com.voyageone.core.ajax.AjaxRequestBean;

/**
 * 画面传入订单检索条件bean
 * 
 * @author jacky
 *
 */
public class InFormSearch extends AjaxRequestBean {
	/**
	 * 订单号
	 */
	private String orderNumber;
	/**
	 * Web订单号
	 */
	private String sourceOrderId;
	/*
	*//**
	 * 订单号(开始)
	 *//*
	private String orderNumberFrom;
	
	*//**
	 * 订单号(结束)
	 *//*
	private String orderNumberTo;
	
	*//**
	 * 源订单号(开始)
	 *//*
	private String sourceOrderIdFrom;
	
	*//**
	 * 源订单号(结束)
	 *//*
	private String sourceOrderIdTo;
	*/
	
	/**
	 * 订单日期(开始)
	 */
	private String orderDateFrom;
	
	/**
	 * 订单日期(结束)
	 */
	private String orderDateTo;

	/*
	*//**
	 * 交易日期(开始)
	 *//*
	private String transactionDateFrom;
	
	*//**
	 * 交易日期(结束)
	 *//*
	private String transactionDateTo;
	
	*//**
	 * 期望发货日期(开始)
	 *//*
	private String expShipDateFrom;
	
	*//**
	 * 期望发货日期(结束)
	 *//*
	private String expShipDateTo;
	
	*//**
	 * 实际发货日期(开始)
	 *//*
	private String actualShipDateFrom;
	
	*//**
	 * 实际发货日期(结束)
	 *//*
	private String actualShipDateTo;
	*/

	/**
	 * 姓名(bill to)
	 */
	private String name;
	
	/**
	 * 姓名(ship to)
	 */
	private String shipName;
	/*
	*//**
	 * 公司(bill to)
	 *//*
	private String company;
	
	*//**
	 * 公司(ship to)
	 *//*
	private String shipCompany;
	*/
	/**
	 * 城市(bill to)
	 */
	private String city;
	
	/**
	 * 城市(ship to)
	 */
	private String shipCity;
	
	/**
	 * 省(bill to)
	 */
	private String state;
	
	/**
	 * 省(ship to)
	 */
	private String shipState;

	/*
	*//**
	 * 国家(bill to)
	 *//*
	private String country;
	
	*//**
	 * 国家(ship to)
	 *//*
	private String shipCountry;
	*/

	/**
	 * 支付状态（实际 payment_total - expected）
	 */
	private String paymentStatus;
	
	/**
	 * 支付状态（预计 final_grand_total - expected）
	 */
	private String transactionStatus;
	
	/**
	 * 支付方式
	 */
	private String paymentMethod;
	
	/**
	 * 订单状态（overSold）
	 */
	private String orderWithOverSold;
	
	/**
	 * 订单状态（canceled）
	 */
	private String orderWithCanceled;
	
	/**
	 * 店铺Id
	 */
	private List<String> storeId;
	
	/**
	 * 渠道Id
	 */
	private List<String> channelId;
	
	/**
	 * Quick Filter
	 */
	private String quickFilter;
	
	/**
	 * 快递方式
	 */
	private String shippingMethod;
	
	/**
	 * 快递单号
	 */
	private String trackingNumber;
	
	/*
	*//**
	 * 下单之后还没发货天数
	 *//*
	private String noShippedDays;
	
	*//**
	 * customer First Name
	 *//*
	private String customerFirstName;
	
	*//**
	 * customer Middle Name
	 *//*
	private String customerMiddleName;
	
	*//**
	 * customer Last Name
	 *//*
	private String customerLastName;
	
	*//**
	 * customer Email
	 *//*
	private String customerEmail;
	*/

	/**
	 * customer Id
	 */
	private String customerId;
	
	/**
	 * sku starts with
	 */
	private String skuStartsWith;
	
	/**
	 * sku includes
	 */
	private String skuIncludes;

	/**
	 * order Status
	 */
	private String orderStatus;
	
	/**
	 * local ship on hold
	 */
	private String localShipOnHold;
	
	/**
	 * invoice
	 */
	private String invoice;

	/**
	 * 电话(bill to)
	 */
	private String phone;
	
	/**
	 * 电话(ship to)
	 */
	private String shipPhone;

	/**
	 * Grand Total Amount(开始)
	 */
	private String grandTotalAmountFrom;

	/**
	 * Grand Total Amount(结束)
	 */
	private String grandTotalAmountTo;
	/**
	 * approved
	 */
    private String approved;
    /**
     * Quick Filter Today Order所需要的条件
     */
    private String fromTime;
    /**
     * 
     */
    private String endTime;
    /**
     * isRefund
     */
    private String isRefund;    
    
    /**
     * 画面用propertyId
     */
    private String propertySelected;
    
    /**
     * 画面用cartId
     */
    private String shoppingCartSelected;
    
    private String orderStatusReturnRequested;
    /**
     * 订单类型
     */
    private String orderKindOriginal;
    
    private String orderKindSplit;
    
    private String orderKindPresent;
    
    private String orderKindExchange;
    
    private String orderKindPriceDifference;
    
    private List<String> orderKindList;
    /**
     * 
     * 第三方订单号
     */
    private String clientOrderId;
	public String getPropertySelected() {
		return propertySelected;
	}

	public void setPropertySelected(String propertySelected) {
		this.propertySelected = propertySelected;
	}

	public String getShoppingCartSelected() {
		return shoppingCartSelected;
	}

	public void setShoppingCartSelected(String shoppingCartSelected) {
		this.shoppingCartSelected = shoppingCartSelected;
	}

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the OrderNumber to set
	 */
	public void setOrderNumber(String orderNumber){
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the SourceOrderId
	 */
	public String getSourceOrderId() {
		return sourceOrderId;
	}

	/**
	 * @param sourceOrderId the SourceOrderId to set
	 */
	public void setSourceOrderId(String sourceOrderId){
		this.sourceOrderId = sourceOrderId;
	}

	/**
	 * @return the orderDateFrom
	 */
	public String getOrderDateFrom() {
		return orderDateFrom;
	}

	/**
	 * @param orderDateFrom the orderDateFrom to set
	 */
	public void setOrderDateFrom(String orderDateFrom) {
		this.orderDateFrom = orderDateFrom;
	}

	/**
	 * @return the orderDateTo
	 */
	public String getOrderDateTo() {
		return orderDateTo;
	}

	/**
	 * @param orderDateTo the orderDateTo to set
	 */
	public void setOrderDateTo(String orderDateTo) {
		this.orderDateTo = orderDateTo;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the shipName
	 */
	public String getShipName() {
		return shipName;
	}

	/**
	 * @param shipName the shipName to set
	 */
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the shipCity
	 */
	public String getShipCity() {
		return shipCity;
	}

	/**
	 * @param shipCity the shipCity to set
	 */
	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the shipState
	 */
	public String getShipState() {
		return shipState;
	}

	/**
	 * @param shipState the shipState to set
	 */
	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	/**
	 * @return the paymentStatus
	 */
	public String getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * @param paymentStatus the paymentStatus to set
	 */
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
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

	/**
	 * @return the orderWithOverSold
	 */
	public String getOrderWithOverSold() {
		return orderWithOverSold;
	}

	/**
	 * @param orderWithOverSold the orderWithOverSold to set
	 */
	public void setOrderWithOverSold(String orderWithOverSold) {
		this.orderWithOverSold = orderWithOverSold;
	}

	/**
	 * @return the orderWithCanceled
	 */
	public String getOrderWithCanceled() {
		return orderWithCanceled;
	}

	/**
	 * @param orderWithCanceled the orderWithCanceled to set
	 */
	public void setOrderWithCanceled(String orderWithCanceled) {
		this.orderWithCanceled = orderWithCanceled;
	}

	/**
	 * @return the storeId
	 */
	public List<String> getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(List<String> storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the channelId
	 */
	public List<String> getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(List<String> channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the quickFilter
	 */
	public String getQuickFilter() {
		return quickFilter;
	}

	/**
	 * @param quickFilter the quickFilter to set
	 */
	public void setQuickFilter(String quickFilter) {
		this.quickFilter = quickFilter;
	}

	/**
	 * @return the shippingMethod
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @param shippingMethod the shippingMethod to set
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @return the trackingNumber
	 */
	public String getTrackingNumber() {
		return trackingNumber;
	}

	/**
	 * @param trackingNumber the trackingNumber to set
	 */
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the skuStartsWith
	 */
	public String getSkuStartsWith() {
		return skuStartsWith;
	}

	/**
	 * @param skuStartsWith the skuStartsWith to set
	 */
	public void setSkuStartsWith(String skuStartsWith) {
		this.skuStartsWith = skuStartsWith;
	}

	/**
	 * @return the skuIncludes
	 */
	public String getSkuIncludes() {
		return skuIncludes;
	}

	/**
	 * @param skuIncludes the skuIncludes to set
	 */
	public void setSkuIncludes(String skuIncludes) {
		this.skuIncludes = skuIncludes;
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

	/**
	 * @return the localShipOnHold
	 */
	public String getLocalShipOnHold() {
		return localShipOnHold;
	}

	/**
	 * @param localShipOnHold the localShipOnHold to set
	 */
	public void setLocalShipOnHold(String localShipOnHold) {
		this.localShipOnHold = localShipOnHold;
	}

	/**
	 * @return the invoice
	 */
	public String getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the shipPhone
	 */
	public String getShipPhone() {
		return shipPhone;
	}

	/**
	 * @param shipPhone the shipPhone to set
	 */
	public void setShipPhone(String shipPhone) {
		this.shipPhone = shipPhone;
	}


	public String getGrandTotalAmountFrom() {
		return grandTotalAmountFrom;
	}

	public void setGrandTotalAmountFrom(String grandTotalAmountFrom) {
		this.grandTotalAmountFrom = grandTotalAmountFrom;
	}

	public String getGrandTotalAmountTo() {
		return grandTotalAmountTo;
	}

	public void setGrandTotalAmountTo(String grandTotalAmountTo) {
		this.grandTotalAmountTo = grandTotalAmountTo;
	}
	
	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}
	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}
	
	public String getOrderStatusReturnRequested() {
		return orderStatusReturnRequested;
	}

	public void setOrderStatusReturnRequested(String orderStatusReturnRequested) {
		this.orderStatusReturnRequested = orderStatusReturnRequested;
	}
	

	
	public String getOrderKindOriginal() {
		return orderKindOriginal;
	}

	
	public void setOrderKindOriginal(String orderKindOriginal) {
		this.orderKindOriginal = orderKindOriginal;
	}


	public String getOrderKindSplit() {
		return orderKindSplit;
	}

	
	public void setOrderKindSplit(String orderKindSplit) {
		this.orderKindSplit = orderKindSplit;
	}

	
	public String getOrderKindPresent() {
		return orderKindPresent;
	}

	
	public void setOrderKindPresent(String orderKindPresent) {
		this.orderKindPresent = orderKindPresent;
	}

	
	public String getOrderKindExchange() {
		return orderKindExchange;
	}

	
	public void setOrderKindExchange(String orderKindExchange) {
		this.orderKindExchange = orderKindExchange;
	}

	
	public String getOrderKindPriceDifference() {
		return orderKindPriceDifference;
	}

	public void setOrderKindPriceDifference(String orderKindPriceDifference) {
		this.orderKindPriceDifference = orderKindPriceDifference;
	}

	public List<String> getOrderKindList() {
		return orderKindList;
	}

	public void setOrderKindList(List<String> orderKindList) {
		this.orderKindList = orderKindList;
	}
    
	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"orderNumber", "paymentStatus"};
	}
}
