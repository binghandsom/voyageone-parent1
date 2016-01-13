package com.voyageone.cms.modelbean;

import java.math.BigDecimal;
import java.util.Date;

public class RelationPromotionProduct {

    private String name;

    private String effectiveDateStart;

    private String effectiveDateEnd;

    private String discountPercent;

    private String discountSalePrice;
    
    private String currentPrice;

    private String isSelectionOver;

    private String isSignUpOver;

    private String isIsolationStockOver;

    private String isActivityOver;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;
    
    private Integer promotionId;

    private Integer productId;
    
    private String promotionYear;
    
    private String promotionMonth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsSelectionOver() {
        return isSelectionOver;
    }

    public void setIsSelectionOver(String isSelectionOver) {
        this.isSelectionOver = isSelectionOver;
    }

    public String getIsSignUpOver() {
        return isSignUpOver;
    }

    public void setIsSignUpOver(String isSignUpOver) {
        this.isSignUpOver = isSignUpOver;
    }

    public String getIsIsolationStockOver() {
        return isIsolationStockOver;
    }

    public void setIsIsolationStockOver(String isIsolationStockOver) {
        this.isIsolationStockOver = isIsolationStockOver;
    }

    public String getIsActivityOver() {
        return isActivityOver;
    }

    public void setIsActivityOver(String isActivityOver) {
        this.isActivityOver = isActivityOver;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

   
    public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getDiscountSalePrice() {
		return discountSalePrice;
	}

	public void setDiscountSalePrice(String discountSalePrice) {
		this.discountSalePrice = discountSalePrice;
	}

	public String getPromotionYear() {
		return promotionYear;
	}
    
	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
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

	public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }
}