package com.voyageone.cms.formbean;

public class ProductUSAmazonPriceInfo extends ProductBaseBean {
	
	private String cartId;
	
	private String freeShippingTypeId;
	 
	 private String price;
	 
	 private String profit;
	 
	 private Boolean isOnSale;
	 	 
	 private String publishDatetime;
	 
	 private String noApproveComment;
	 
	 private String msrp;
	 
	 private Boolean isApproved;
	 
	 private String prePublishDatetime;

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getFreeShippingTypeId() {
		return freeShippingTypeId;
	}

	public void setFreeShippingTypeId(String freeShippingTypeId) {
		this.freeShippingTypeId = freeShippingTypeId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getPublishDatetime() {
		return publishDatetime;
	}

	public void setPublishDatetime(String publishDatetime) {
		this.publishDatetime = publishDatetime;
	}

	public String getNoApproveComment() {
		return noApproveComment;
	}

	public void setNoApproveComment(String noApproveComment) {
		this.noApproveComment = noApproveComment;
	}

	public String getMsrp() {
		return msrp;
	}

	public void setMsrp(String msrp) {
		this.msrp = msrp;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getPrePublishDatetime() {
		return prePublishDatetime;
	}

	public void setPrePublishDatetime(String prePublishDatetime) {
		this.prePublishDatetime = prePublishDatetime;
	}

	/**
	 * @return the isOnSale
	 */
	public Boolean getIsOnSale() {
		return isOnSale;
	}

	/**
	 * @param isOnSale the isOnSale to set
	 */
	public void setIsOnSale(Boolean isOnSale) {
		this.isOnSale = isOnSale;
	}

}
