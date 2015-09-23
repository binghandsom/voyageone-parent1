package com.voyageone.cms.formbean;

public class ProductUSPriceInfo extends ProductBaseBean{

	private String cartId;

	private String freeShippingTypeId;

	private String price;

	private String currentPrice;

	private String profit;

	private Boolean isOnSale;

	private Boolean isOutletsOnSale;

	private String publishDatetime;

	private String prePublishDatetime;

	private String noApproveComment;

	private String msrp;
	
	private String code;

	private String discount;

	private String prePublishBeforeSharingDays;

	private Boolean isApproved;

	private Boolean currentIsApproved;

	private String approvedDatetime;

	private String comment;

	private String priceType;

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

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

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
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

	/**
	 * @return the isOutletsOnSale
	 */
	public Boolean getIsOutletsOnSale() {
		return isOutletsOnSale;
	}

	/**
	 * @param isOutletsOnSale the isOutletsOnSale to set
	 */
	public void setIsOutletsOnSale(Boolean isOutletsOnSale) {
		this.isOutletsOnSale = isOutletsOnSale;
	}

	/**
	 * @return the isApproved
	 */
	public Boolean getIsApproved() {
		return isApproved;
	}

	/**
	 * @param isApproved the isApproved to set
	 */
	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
		this.setCurrentIsApproved(isApproved);
	}

	public String getPrePublishDatetime() {
		return prePublishDatetime;
	}

	public void setPrePublishDatetime(String prePublishDatetime) {
		this.prePublishDatetime = prePublishDatetime;
	}

	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}

	/**
	 * @return the prePublishBeforeSharingDays
	 */
	public String getPrePublishBeforeSharingDays() {
		return prePublishBeforeSharingDays;
	}

	/**
	 * @param prePublishBeforeSharingDays the prePublishBeforeSharingDays to set
	 */
	public void setPrePublishBeforeSharingDays(String prePublishBeforeSharingDays) {
		this.prePublishBeforeSharingDays = prePublishBeforeSharingDays;
	}

	/**
	 * @return the approvedDatetime
	 */
	public String getApprovedDatetime() {
		return approvedDatetime;
	}

	/**
	 * @param approvedDatetime the approvedDatetime to set
	 */
	public void setApprovedDatetime(String approvedDatetime) {
		this.approvedDatetime = approvedDatetime;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the currentPrice
	 */
	public String getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	/**
	 * @return the currentIsApproved
	 */
	public Boolean getCurrentIsApproved() {
		return currentIsApproved;
	}

	/**
	 * @param currentIsApproved the currentIsApproved to set
	 */
	public void setCurrentIsApproved(Boolean currentIsApproved) {
		this.currentIsApproved = currentIsApproved;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	
}
