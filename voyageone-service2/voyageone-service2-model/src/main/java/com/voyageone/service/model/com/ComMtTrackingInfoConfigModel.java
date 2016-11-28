/*
 * ComMtTrackingInfoConfigModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.com;

/**
 * 
 */
public class ComMtTrackingInfoConfigModel extends AdminBaseModel {
    protected Integer seq;

    protected String orderChannelId;

    protected Integer cartId;

    protected String trackingStatus;

    protected String trackingInfo;

    protected String location;

    protected String displayFlg;

    protected String displayStatus;

    /**
     * 快递区域
0：国内快递
1：国际快递
     */
    protected String trackingArea;

    /**
     * 物流展开标志位，1时会将运单号相关的路由信息也取得
     */
    protected String trackingSpreadFlg;

    protected String comment;

    protected Boolean active;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId == null ? null : orderChannelId.trim();
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus == null ? null : trackingStatus.trim();
    }

    public String getTrackingInfo() {
        return trackingInfo;
    }

    public void setTrackingInfo(String trackingInfo) {
        this.trackingInfo = trackingInfo == null ? null : trackingInfo.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getDisplayFlg() {
        return displayFlg;
    }

    public void setDisplayFlg(String displayFlg) {
        this.displayFlg = displayFlg == null ? null : displayFlg.trim();
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus == null ? null : displayStatus.trim();
    }

    public String getTrackingArea() {
        return trackingArea;
    }

    public void setTrackingArea(String trackingArea) {
        this.trackingArea = trackingArea == null ? null : trackingArea.trim();
    }

    public String getTrackingSpreadFlg() {
        return trackingSpreadFlg;
    }

    public void setTrackingSpreadFlg(String trackingSpreadFlg) {
        this.trackingSpreadFlg = trackingSpreadFlg == null ? null : trackingSpreadFlg.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}