package com.voyageone.components.eexpress.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("awb")
public class Awb {
	
	private String userToken;
	private String customerHawb;
	private String shipmentDate;
	private String rName;
	private String rCountry;
	private String rProvince;
	private String rAddress1;
	private String rAddress2;
	private String rCity;
	private String rZip;
	private String rTel;
	private int Pieces;
	private Double weight;
	private String dCurrency;
	private Double dValue;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getCustomerHawb() {
		return customerHawb;
	}
	public void setCustomerHawb(String customerHawb) {
		this.customerHawb = customerHawb;
	}
	public String getShipmentDate() {
		return shipmentDate;
	}
	public void setShipmentDate(String shipmentDate) {
		this.shipmentDate = shipmentDate;
	}
	public String getrName() {
		return rName;
	}
	public void setrName(String rName) {
		this.rName = rName;
	}
	public String getrCountry() {
		return rCountry;
	}
	public void setrCountry(String rCountry) {
		this.rCountry = rCountry;
	}
	public String getrProvince() {
		return rProvince;
	}
	public void setrProvince(String rProvince) {
		this.rProvince = rProvince;
	}
	public String getrAddress1() {
		return rAddress1;
	}
	public void setrAddress1(String rAddress1) {
		this.rAddress1 = rAddress1;
	}
	public String getrAddress2() {
		return rAddress2;
	}
	public void setrAddress2(String rAddress2) {
		this.rAddress2 = rAddress2;
	}
	public String getrCity() {
		return rCity;
	}
	public void setrCity(String rCity) {
		this.rCity = rCity;
	}
	public String getrZip() {
		return rZip;
	}
	public void setrZip(String rZip) {
		this.rZip = rZip;
	}
	public String getrTel() {
		return rTel;
	}
	public void setrTel(String rTel) {
		this.rTel = rTel;
	}

	
	public int getPieces() {
		return Pieces;
	}
	public void setPieces(int pieces) {
		Pieces = pieces;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getdCurrency() {
		return dCurrency;
	}
	public void setdCurrency(String dCurrency) {
		this.dCurrency = dCurrency;
	}
	public Double getdValue() {
		return dValue;
	}
	public void setdValue(Double dValue) {
		this.dValue = dValue;
	}

	
}
