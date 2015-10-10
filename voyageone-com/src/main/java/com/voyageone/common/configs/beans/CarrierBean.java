package com.voyageone.common.configs.beans;

public class CarrierBean {
	
	// 渠道（MAPPING KEY）
	private String order_channel_id;
	// 快递公司名（MAPPING KEY）
	private String carrier;
	
	// api url
	private String api_url;
	// api code
	private String api_key;
	// api user
	private String api_user;
	// api pwd
	private String api_pwd;

	// 卡号
	private String card_number;
	// cusite
	private String cusite;
	// cusname
	private String cusname;
	// customer
	private String customer;
	// alias_name
	private String alias_name;
	// wsdl_url
	private String wsdl_url;
	// comments
	private String comments;

	public String getOrder_channel_id() {
		return order_channel_id;
	}
	public void setOrder_channel_id(String orderChannelId) {
		order_channel_id = orderChannelId;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public String getApi_url() {
		return api_url;
	}
	public void setApi_url(String apiUrl) {
		api_url = apiUrl;
	}
	public String getApi_key() {
		return api_key;
	}
	public void setApi_key(String apiKey) {
		api_key = apiKey;
	}
	public String getApi_user() {
		return api_user;
	}
	public void setApi_user(String apiUser) {
		api_user = apiUser;
	}
	public String getApi_pwd() {
		return api_pwd;
	}
	public void setApi_pwd(String apiPwd) {
		api_pwd = apiPwd;
	}
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String cardNumber) {
		card_number = cardNumber;
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
	public String getAlias_name() {
		return alias_name;
	}
	public void setAlias_name(String aliasName) {
		alias_name = aliasName;
	}
	public String getWsdl_url() {
		return wsdl_url;
	}
	public void setWsdl_url(String wsdlUrl) {
		wsdl_url = wsdlUrl;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
