package com.voyageone.cms.modelbean;

import java.util.Date;

import com.voyageone.cms.formbean.BaseBean;

public class Promotion extends BaseBean{
    private String cartId;
    
    private String cart;

    private String name;

    private String preheatDateStart;

    private String preheatDateEnd;
    
    private String preheatDate;

    private String effectiveDateStart;

    private String effectiveDateEnd;
    
    private String effectiveDate;

    private Boolean isEffective;

    private Boolean isSelectionOver;

    private Boolean isSignUpOver;

    private Boolean isIsolationStockOver;

    private Boolean isActivityOver;
    
    private String promotionYear;
    
    private String promotionMonth;
    
    private String attachment;

    private String comment;
    
    private String promotionId;

    private String channelId;

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreheatDateStart() {
		return preheatDateStart;
	}

	public void setPreheatDateStart(String preheatDateStart) {
		this.preheatDateStart = preheatDateStart;
	}

	public String getPreheatDateEnd() {
		return preheatDateEnd;
	}

	public void setPreheatDateEnd(String preheatDateEnd) {
		this.preheatDateEnd = preheatDateEnd;
	}

	public String getEffectiveDateStart() {
		return effectiveDateStart;
	}

	public void setEffectiveDateStart(String effectiveDateStart) {
		this.effectiveDateStart = effectiveDateStart;
	}

	public String getEffectiveDateEnd() {
		return effectiveDateEnd;
	}

	public void setEffectiveDateEnd(String effectiveDateEnd) {
		this.effectiveDateEnd = effectiveDateEnd;
	}
    
	public String getPromotionYear() {
		return promotionYear;
	}

	public void setPromotionYear(String promotionYear) {
		this.promotionYear = promotionYear;
	}

	public String getPromotionMonth() {
		return promotionMonth;
	}

	public void setPromotionMonth(String promotionMonth) {
		this.promotionMonth = promotionMonth;
	}
    
	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Boolean getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(Boolean isEffective) {
		this.isEffective = isEffective;
	}

	public Boolean getIsSelectionOver() {
		return isSelectionOver;
	}

	public void setIsSelectionOver(Boolean isSelectionOver) {
		this.isSelectionOver = isSelectionOver;
	}

	public Boolean getIsSignUpOver() {
		return isSignUpOver;
	}

	public void setIsSignUpOver(Boolean isSignUpOver) {
		this.isSignUpOver = isSignUpOver;
	}

	public Boolean getIsIsolationStockOver() {
		return isIsolationStockOver;
	}

	public void setIsIsolationStockOver(Boolean isIsolationStockOver) {
		this.isIsolationStockOver = isIsolationStockOver;
	}

	public Boolean getIsActivityOver() {
		return isActivityOver;
	}

	public void setIsActivityOver(Boolean isActivityOver) {
		this.isActivityOver = isActivityOver;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCart() {
		return cart;
	}

	public void setCart(String cart) {
		this.cart = cart;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate ) {
		this.effectiveDate = effectiveDate;
	}

	public String getPreheatDate() {
		return preheatDate;
	}

	public void setPreheatDate(String preheatDate) {
		this.preheatDate = preheatDate;
	}
	
	
}