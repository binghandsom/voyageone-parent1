package com.voyageone.service.model.cms.mongo.jm.promotion;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;

/**
 * 按渠道拆分的，聚美专场活动使用的，图片数据模型
 *
 * Created by jonas on 2016/10/14.
 *
 * @author jonas
 * @version 2.8.0
 * @since 2.8.0
 */
public class CmsBtJmPromotionImagesModel extends ChannelPartitionModel {

    private Integer cartId;

    private Integer promotionId;

    private Integer jmPromotionId;

    /**
     * 备注
     */
    private String comment;

    /**
     * 是否使用模板
     */
    private Boolean useTemplate;

    /**
     * app 入口图样式 (2)
     */
    private List<String> appEnterance;

    /**
     * 详情页、心愿单页面、专场入口图 (2)
     */
    private List<String> appDetailEnterance;

    /**
     * app 端分享图
     */
    private String appShare;

    /**
     * app 端专场头图
     */
    private String appHeader;

    /**
     * 频道页大图 (2)
     */
    private List<String> pcChannelBig;

    /**
     * 频道页中图 (2)
     */
    private List<String> pcChannelMiddle;

    /**
     * 频道页小图
     */
    private String pcChannelSmall;

    /**
     * 美妆品牌团入口图 (3)
     */
    private List<String> pcBrandEnterance;

    /**
     * 手机焦点图
     */
    private String mobileForce;

    /**
     * pc 端专场头图
     */
    private String pcHeader;

    /**
     * pc 端轮播
     */
    private String pcCarousel;

    /**
     * app 端轮播
     */
    private String appCarousel;

    /**
     * PC 一排 2 个
     */
    private String pcChannelHeader;

    /**
     * app 端大卡片位左
     */
    private String appLeftBigCard;

    /**
     * app 端大卡片位右
     */
    private String appRightBigCard;

    /**
     * app 小卡片位
     */
    private String appSmallCard;

    /**
     * pc 端首页正式图预热
     */
    private String pcIndexPreheat;

    /**
     * pc 端首页正式图
     */
    private String pcIndex;

    /**
     * ipad 轮播
     */
    private String padCarousel;

    /**
     * ipad 端卡片位预热
     */
    private String padCardPreheat;

    /**
     * ipad 端卡片位
     */
    private String padCard;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getJmPromotionId() {
        return jmPromotionId;
    }

    public void setJmPromotionId(Integer jmPromotionId) {
        this.jmPromotionId = jmPromotionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getUseTemplate() {
        return useTemplate;
    }

    public void setUseTemplate(Boolean useTemplate) {
        this.useTemplate = useTemplate;
    }

    public List<String> getAppEnterance() {
        return appEnterance;
    }

    public void setAppEnterance(List<String> appEnterance) {
        this.appEnterance = appEnterance;
    }

    public List<String> getAppDetailEnterance() {
        return appDetailEnterance;
    }

    public void setAppDetailEnterance(List<String> appDetailEnterance) {
        this.appDetailEnterance = appDetailEnterance;
    }

    public String getAppShare() {
        return appShare;
    }

    public void setAppShare(String appShare) {
        this.appShare = appShare;
    }

    public String getAppHeader() {
        return appHeader;
    }

    public void setAppHeader(String appHeader) {
        this.appHeader = appHeader;
    }

    public List<String> getPcChannelBig() {
        return pcChannelBig;
    }

    public void setPcChannelBig(List<String> pcChannelBig) {
        this.pcChannelBig = pcChannelBig;
    }

    public List<String> getPcChannelMiddle() {
        return pcChannelMiddle;
    }

    public void setPcChannelMiddle(List<String> pcChannelMiddle) {
        this.pcChannelMiddle = pcChannelMiddle;
    }

    public String getPcChannelSmall() {
        return pcChannelSmall;
    }

    public void setPcChannelSmall(String pcChannelSmall) {
        this.pcChannelSmall = pcChannelSmall;
    }

    public List<String> getPcBrandEnterance() {
        return pcBrandEnterance;
    }

    public void setPcBrandEnterance(List<String> pcBrandEnterance) {
        this.pcBrandEnterance = pcBrandEnterance;
    }

    public String getMobileForce() {
        return mobileForce;
    }

    public void setMobileForce(String mobileForce) {
        this.mobileForce = mobileForce;
    }

    public String getPcHeader() {
        return pcHeader;
    }

    public void setPcHeader(String pcHeader) {
        this.pcHeader = pcHeader;
    }

    public String getPcCarousel() {
        return pcCarousel;
    }

    public void setPcCarousel(String pcCarousel) {
        this.pcCarousel = pcCarousel;
    }

    public String getAppCarousel() {
        return appCarousel;
    }

    public void setAppCarousel(String appCarousel) {
        this.appCarousel = appCarousel;
    }

    public String getPcChannelHeader() {
        return pcChannelHeader;
    }

    public void setPcChannelHeader(String pcChannelHeader) {
        this.pcChannelHeader = pcChannelHeader;
    }

    public String getAppLeftBigCard() {
        return appLeftBigCard;
    }

    public void setAppLeftBigCard(String appLeftBigCard) {
        this.appLeftBigCard = appLeftBigCard;
    }

    public String getAppRightBigCard() {
        return appRightBigCard;
    }

    public void setAppRightBigCard(String appRightBigCard) {
        this.appRightBigCard = appRightBigCard;
    }

    public String getAppSmallCard() {
        return appSmallCard;
    }

    public void setAppSmallCard(String appSmallCard) {
        this.appSmallCard = appSmallCard;
    }

    public String getPcIndexPreheat() {
        return pcIndexPreheat;
    }

    public void setPcIndexPreheat(String pcIndexPreheat) {
        this.pcIndexPreheat = pcIndexPreheat;
    }

    public String getPcIndex() {
        return pcIndex;
    }

    public void setPcIndex(String pcIndex) {
        this.pcIndex = pcIndex;
    }

    public String getPadCarousel() {
        return padCarousel;
    }

    public void setPadCarousel(String padCarousel) {
        this.padCarousel = padCarousel;
    }

    public String getPadCardPreheat() {
        return padCardPreheat;
    }

    public void setPadCardPreheat(String padCardPreheat) {
        this.padCardPreheat = padCardPreheat;
    }

    public String getPadCard() {
        return padCard;
    }

    public void setPadCard(String padCard) {
        this.padCard = padCard;
    }
}
