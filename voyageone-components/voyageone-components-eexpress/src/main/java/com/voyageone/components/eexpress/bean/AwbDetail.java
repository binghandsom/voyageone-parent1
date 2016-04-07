package com.voyageone.components.eexpress.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("awbDetail")
public class AwbDetail {
	private String itemDescription;
	private String hsCode;
	private double itemPrice;
	private int itemPieces;
	private Double itemWeight;
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getHsCode() {
		return hsCode;
	}
	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	public double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public int getItemPieces() {
		return itemPieces;
	}
	public void setItemPieces(int itemPieces) {
		this.itemPieces = itemPieces;
	}
	public Double getItemWeight() {
		return itemWeight;
	}
	public void setItemWeight(Double itemWeight) {
		this.itemWeight = itemWeight;
	}
	
	
}
