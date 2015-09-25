package com.voyageone.oms.modelbean;


public class RateBean {

	//	订单渠道ID
	private String orderChannelId;
	//	CartID
	private String cartId;
	//	汇率时间
	private String rateTime;
	//	上传日期
	private String rate;
	//	币种
	private String currency;
	//	误差
	private String calculationError;

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

	public String getRateTime() {
		return rateTime;
	}

	public void setRateTime(String rateTime) {
		this.rateTime = rateTime;
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

	public String getCalculationError() {
		return calculationError;
	}

	public void setCalculationError(String calculationError) {
		this.calculationError = calculationError;
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
