package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtPromotionModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private int cartId;
    /**
     * 0:Open 1:Close
     */
    private int promotionStatus;
    /**

     */
    private String promotionName;
    /**

     */
    private String preSaleEnd;
    /**

     */
    private String preSaleStart;
    /**
     * 预热开始时间
     */
    private String prePeriodStart;
    /**
     * 预热结束时间
     */
    private String prePeriodEnd;
    /**
     * 活动开始
     */
    private String activityStart;
    /**
     * 活动结束
     */
    private String activityEnd;
    /**
     * 0代表不适用特价宝
     */
    private String tejiabaoId;
    /**
     * 1:促销  2:批量刷价格 3:价格披露 4:库存隔离
     */
    private String promotionType;
    /**

     */
    private int refTagId;
    /**

     */
    private int active;
    /**
     * 是否是全店特价宝的活动
     */
    private int isAllPromotion;


    /**

     */
    public String getChannelId() {

        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }

    }


    /**

     */
    public int getCartId() {

        return this.cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


    /**
     * 0:Open 1:Close
     */
    public int getPromotionStatus() {

        return this.promotionStatus;
    }

    public void setPromotionStatus(int promotionStatus) {
        this.promotionStatus = promotionStatus;
    }


    /**

     */
    public String getPromotionName() {

        return this.promotionName;
    }

    public void setPromotionName(String promotionName) {
        if (promotionName != null) {
            this.promotionName = promotionName;
        } else {
            this.promotionName = "";
        }

    }


    /**

     */
    public String getPreSaleEnd() {

        return this.preSaleEnd;
    }

    public void setPreSaleEnd(String preSaleEnd) {
        if (preSaleEnd != null) {
            this.preSaleEnd = preSaleEnd;
        } else {
            this.preSaleEnd = "";
        }

    }


    /**

     */
    public String getPreSaleStart() {

        return this.preSaleStart;
    }

    public void setPreSaleStart(String preSaleStart) {
        if (preSaleStart != null) {
            this.preSaleStart = preSaleStart;
        } else {
            this.preSaleStart = "";
        }

    }


    /**
     * 预热开始时间
     */
    public String getPrePeriodStart() {

        return this.prePeriodStart;
    }

    public void setPrePeriodStart(String prePeriodStart) {
        if (prePeriodStart != null) {
            this.prePeriodStart = prePeriodStart;
        } else {
            this.prePeriodStart = "";
        }

    }


    /**
     * 预热结束时间
     */
    public String getPrePeriodEnd() {

        return this.prePeriodEnd;
    }

    public void setPrePeriodEnd(String prePeriodEnd) {
        if (prePeriodEnd != null) {
            this.prePeriodEnd = prePeriodEnd;
        } else {
            this.prePeriodEnd = "";
        }

    }


    /**
     * 活动开始
     */
    public String getActivityStart() {

        return this.activityStart;
    }

    public void setActivityStart(String activityStart) {
        if (activityStart != null) {
            this.activityStart = activityStart;
        } else {
            this.activityStart = "";
        }

    }


    /**
     * 活动结束
     */
    public String getActivityEnd() {

        return this.activityEnd;
    }

    public void setActivityEnd(String activityEnd) {
        if (activityEnd != null) {
            this.activityEnd = activityEnd;
        } else {
            this.activityEnd = "";
        }

    }


    /**
     * 0代表不适用特价宝
     */
    public String getTejiabaoId() {

        return this.tejiabaoId;
    }

    public void setTejiabaoId(String tejiabaoId) {
        if (tejiabaoId != null) {
            this.tejiabaoId = tejiabaoId;
        } else {
            this.tejiabaoId = "";
        }

    }


    /**
     * 1:促销  2:批量刷价格 3:价格披露 4:库存隔离
     */
    public String getPromotionType() {

        return this.promotionType;
    }

    public void setPromotionType(String promotionType) {
        if (promotionType != null) {
            this.promotionType = promotionType;
        } else {
            this.promotionType = "";
        }

    }


    /**

     */
    public int getRefTagId() {

        return this.refTagId;
    }

    public void setRefTagId(int refTagId) {
        this.refTagId = refTagId;
    }


    /**

     */
    public int geActive() {

        return this.active;
    }

    public void setActive(int isActive) {
        this.active = isActive;
    }


    /**
     * 是否是全店特价宝的活动
     */
    public int getIsAllPromotion() {

        return this.isAllPromotion;
    }

    public void setIsAllPromotion(int isAllPromotion) {
        this.isAllPromotion = isAllPromotion;
    }


}