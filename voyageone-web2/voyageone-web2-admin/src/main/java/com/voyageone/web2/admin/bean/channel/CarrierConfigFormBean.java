package com.voyageone.web2.admin.bean.channel;

import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/17
 */
public class CarrierConfigFormBean extends AdminFormBean {
	
	private String orderChannelId;
	
	private String carrier;
	
	private String apiKey;
	
	private String apiUser;
	
	private String apiPwd;
	
	private String cardNumber;
	
	private String cusite;
	
	private String cusname;
	
	private String customer;
	
	private String usekd100Flg;
	
	private String wsdlUrl;
	
	private String comments;
	
	private Boolean active;

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiUser() {
		return apiUser;
	}

	public void setApiUser(String apiUser) {
		this.apiUser = apiUser;
	}

	public String getApiPwd() {
		return apiPwd;
	}

	public void setApiPwd(String apiPwd) {
		this.apiPwd = apiPwd;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCusite() {
		return cusite;
	}

	public void setCusite(String cusite) {
		this.cusite = cusite;
	}

	public String getCusname() {
		return cusname;
	}

	public void setCusname(String cusname) {
		this.cusname = cusname;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getUsekd100Flg() {
		return usekd100Flg;
	}

	public void setUsekd100Flg(String usekd100Flg) {
		this.usekd100Flg = usekd100Flg;
	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
