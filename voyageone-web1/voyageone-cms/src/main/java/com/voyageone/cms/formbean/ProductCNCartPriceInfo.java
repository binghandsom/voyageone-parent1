package com.voyageone.cms.formbean;

public class ProductCNCartPriceInfo extends ProductBaseBean{
	
	private String cartId;

 	private String freeShippingTypeId;

    private String cnPriceAdjustmentRmb;

    private String cnPriceFinalRmb;

    private String cnPriceFinalRmbDiscount;

    private String prePublishDatetime;

    private boolean isOnSale;

    private boolean isApproved;

    private String approvedDatetime;

    private String noApproveComment;
    
    private String numIid;

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

	public String getCnPriceAdjustmentRmb() {
		return cnPriceAdjustmentRmb;
	}

	public void setCnPriceAdjustmentRmb(String cnPriceAdjustmentRmb) {
		this.cnPriceAdjustmentRmb = cnPriceAdjustmentRmb;
	}

	public String getCnPriceFinalRmb() {
		return cnPriceFinalRmb;
	}

	public void setCnPriceFinalRmb(String cnPriceFinalRmb) {
		this.cnPriceFinalRmb = cnPriceFinalRmb;
	}

	public String getCnPriceFinalRmbDiscount() {
		return cnPriceFinalRmbDiscount;
	}

	public void setCnPriceFinalRmbDiscount(String cnPriceFinalRmbDiscount) {
		this.cnPriceFinalRmbDiscount = cnPriceFinalRmbDiscount;
	}

	public String getPrePublishDatetime() {
		return prePublishDatetime;
	}

	public void setPrePublishDatetime(String prePublishDatetime) {
		this.prePublishDatetime = prePublishDatetime;
	}

	public boolean isOnSale() {
		return isOnSale;
	}

	public void setOnSale(boolean isOnSale) {
		this.isOnSale = isOnSale;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getApprovedDatetime() {
		return approvedDatetime;
	}

	public void setApprovedDatetime(String approvedDatetime) {
		this.approvedDatetime = approvedDatetime;
	}

	public String getNoApproveComment() {
		return noApproveComment;
	}

	public void setNoApproveComment(String noApproveComment) {
		this.noApproveComment = noApproveComment;
	}

	public String getNumIid() {
		return numIid;
	}

	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}
    
    

}
