package com.voyageone.service.bean.cms;

import com.voyageone.base.dao.mysql.BaseModel;

import java.math.BigDecimal;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
public class CmsBtPromotionHistoryBean extends BaseModel {
    private int seq;
    private int promotionId;
    private String channelId;
    private int cartId;
    private String name;
    private String shortName;
    private int promotionStatus;
    private String promotionName;
    private String activityStart;
    private String activityEnd;
    private int productId;
    private String productCode;
    private BigDecimal salePrice;
    private BigDecimal msrp;
    private BigDecimal promotionPrice;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getPromotionStatus() {
        return promotionStatus;
    }

    public void setPromotionStatus(int promotionStatus) {
        this.promotionStatus = promotionStatus;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(String activityStart) {
        this.activityStart = activityStart;
    }

    public String getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(String activityEnd) {
        this.activityEnd = activityEnd;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }
}
