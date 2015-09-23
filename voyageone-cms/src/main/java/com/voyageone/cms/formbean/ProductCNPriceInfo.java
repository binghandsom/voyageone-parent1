package com.voyageone.cms.formbean;

import com.voyageone.common.util.StringUtils;

public class ProductCNPriceInfo extends ProductBaseBean{

	    private String cartId;

	 	private String freeShippingTypeId;

	    private String firstSalePrice;
	    
	    private String msrp;
	    
	    private String code;
	    
	    private String referenceMsrp;
	    
	    private String referencePrice;
	    
	    private String currentCnPriceFinalRmb;

	    private String cnPrice;
	    
	    private String currentCnPrice;

	    private String cnPriceDiscount;

	    private String effectivePrice;

	    private String cnPriceRmb;

	    private String cnPriceAdjustmentRmb;

	    private String cnPriceFinalRmb;

	    private String cnPriceFinalRmbDiscount;

	    private String prePublishDatetime;
	    
	    private String publishDatetime;

	    private Boolean isOnSale;

	    private Boolean isOutletsOnSale;
	    
	    private Boolean isApproved;
	    
	    private Boolean currentIsApproved;

	    private String approvedDatetime;


	    private String noApproveComment;

	    private String comment;
	    
	    private String cnGroupId;
	    
	    private String tempFinalPriceRmbDiscount;
	    
	    private String numIid;
	    
		/**
		 * @return the tempFinalPriceRmbDiscount
		 */
		public String getTempFinalPriceRmbDiscount() {
			return tempFinalPriceRmbDiscount;
		}

		/**
		 * @param tempFinalPriceRmbDiscount the tempFinalPriceRmbDiscount to set
		 */
		public void setTempFinalPriceRmbDiscount(String tempFinalPriceRmbDiscount) {
			this.tempFinalPriceRmbDiscount = tempFinalPriceRmbDiscount;
		}

		/**
		 * @return the cnGroupId
		 */
		public String getCnGroupId() {
			return cnGroupId;
		}

		/**
		 * @param cnGroupId the cnGroupId to set
		 */
		public void setCnGroupId(String cnGroupId) {
			this.cnGroupId = cnGroupId;
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

		public String getFirstSalePrice() {
			return firstSalePrice;
		}

		public void setFirstSalePrice(String firstSalePrice) {
			this.firstSalePrice = firstSalePrice;
		}

		public String getCnPrice() {
			return cnPrice;
		}

		public void setCnPrice(String cnPrice) {
			this.cnPrice = cnPrice;
		}

		public String getCnPriceDiscount() {
			return cnPriceDiscount;
		}
        
		public String getMsrp() {
			return msrp;
		}

		public void setMsrp(String msrp) {
			this.msrp = msrp;
		}

		public String getReferenceMsrp() {
			return referenceMsrp;
		}

		public void setReferenceMsrp(String referenceMsrp) {
			this.referenceMsrp = referenceMsrp;
		}

		public String getReferencePrice() {
			return referencePrice;
		}

		public void setReferencePrice(String referencePrice) {
			this.referencePrice = referencePrice;
		}

		public String getCurrentCnPriceFinalRmb() {
			return currentCnPriceFinalRmb;
		}

		public void setCurrentCnPriceFinalRmb(String currentCnPriceFinalRmb) {
			this.currentCnPriceFinalRmb = currentCnPriceFinalRmb;
		}

		public void setCnPriceDiscount(String cnPriceDiscount) {
			this.cnPriceDiscount = cnPriceDiscount;
		}

		public String getEffectivePrice() {
			return effectivePrice;
		}

		public void setEffectivePrice(String effectivePrice) {
			this.effectivePrice = effectivePrice;
		}

		public String getCnPriceRmb() {
			return cnPriceRmb;
		}

		public void setCnPriceRmb(String cnPriceRmb) {
			this.cnPriceRmb = cnPriceRmb;
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
			if(!StringUtils.isEmpty(prePublishDatetime))
			{
				if(prePublishDatetime.length() >= 19){
					prePublishDatetime = prePublishDatetime.substring(0, 19);
				}
			}
			this.prePublishDatetime = prePublishDatetime;
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

		/**
		 * @return the currentCnPrice
		 */
		public String getCurrentCnPrice() {
			return currentCnPrice;
		}

		/**
		 * @param currentCnPrice the currentCnPrice to set
		 */
		public void setCurrentCnPrice(String currentCnPrice) {
			this.currentCnPrice = currentCnPrice;
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
		
		
		public String getPublishDatetime() {
			return publishDatetime;
		}

		public void setPublishDatetime(String publishDatetime) {
			this.publishDatetime = publishDatetime;
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
			setCurrentIsApproved(isApproved);
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

		public String getNumIid() {
			return numIid;
		}

		public void setNumIid(String numIid) {
			this.numIid = numIid;
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
