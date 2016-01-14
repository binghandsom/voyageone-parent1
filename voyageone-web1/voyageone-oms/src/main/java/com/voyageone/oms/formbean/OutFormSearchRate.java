package com.voyageone.oms.formbean;


public class OutFormSearchRate {

	private String orderChannelId;
	//	orderChannelName
	private String orderChannelName;

	private String cartId;
	//	销售渠道
	private String cartName;
	//	汇率
	private String rate;
	//	汇率日期
	private String rateTime;
	//	币种
	private String currency;
	//	计算误差
	private String calculationError;

	public String getOrderChannelName() {
		return orderChannelName;
	}

	public void setOrderChannelName(String orderChannelName) {
		this.orderChannelName = orderChannelName;
	}

	public String getCartName() {
		return cartName;
	}

	public void setCartName(String cartName) {
		this.cartName = cartName;
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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getRateTime() {
		return rateTime;
	}

	public void setRateTime(String rateTime) {
		this.rateTime = rateTime;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCalculationError() {
		return calculationError;
	}

	public void setCalculationError(String calculationError) {
		this.calculationError = calculationError;
	}
}
