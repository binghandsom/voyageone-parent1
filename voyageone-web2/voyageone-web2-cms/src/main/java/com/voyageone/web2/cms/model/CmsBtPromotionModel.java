package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.Date;

public class CmsBtPromotionModel extends BaseMongoModel {
    private Integer promotionId;

    private String channelId;

    private Integer cartId;

    private Boolean promotionStatus;

    private String promotionName;

    private String prePeriodStart;

    private String prePeriodEnd;

    private String activityStart;

    private String activityEnd;

    private String tejiabaoId;

    private String promotionType;

    private String cartName;

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName == null ? null : promotionName.trim();
    }

    public String getPrePeriodStart() {
        return prePeriodStart;
    }

    public void setPrePeriodStart(String prePeriodStart) {
        this.prePeriodStart = prePeriodStart == null ? null : prePeriodStart.trim();
    }

    public String getPrePeriodEnd() {
        return prePeriodEnd;
    }

    public void setPrePeriodEnd(String prePeriodEnd) {
        this.prePeriodEnd = prePeriodEnd == null ? null : prePeriodEnd.trim();
    }

    public String getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(String activityStart) {
        this.activityStart = activityStart == null ? null : activityStart.trim();
    }

    public String getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(String activityEnd) {
        this.activityEnd = activityEnd == null ? null : activityEnd.trim();
    }

    public String getTejiabaoId() {
        return tejiabaoId;
    }

    public void setTejiabaoId(String tejiabaoId) {
        this.tejiabaoId = tejiabaoId == null ? null : tejiabaoId.trim();
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType == null ? null : promotionType.trim();
    }

    public Boolean getPromotionStatus() {
        return promotionStatus;
    }

    public void setPromotionStatus(Boolean promotionStatus) {
        this.promotionStatus = promotionStatus;
    }
}