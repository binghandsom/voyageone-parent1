package com.voyageone.service.model.cms.mongo.jm.promotion;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;

/**
 * 按渠道拆分的，聚美专场活动使用的，图片数据模型
 * <p>
 * Created by jonas on 2016/10/14.
 *
 * @author jonas
 * @version 2.8.0
 * @since 2.8.0
 */
public class CmsBtJmPromotionImagesModel extends ChannelPartitionModel {

    private Integer promotionId;

    private Integer jmPromotionId;

    private String brand;

    /**
     * 备注
     */
    private String comment;

    /**
     * 是否使用模板
     */
    private Boolean useTemplate;

    /**
     * 1-PC端入口图-600×320
     */
    private String pcEntrance;

    /**
     * 2-移动端入口图-和app首页5号图一致-2048x1024
     */
    private String appEntrance;

    /**
     * 3-微信分享图-300x300
     */
    private String wxShare;

    /**
     * 4-频道页封面大图-1000×490
     */
    private String channelFrontCover;

    /**
     * 5-频道页中图-640×420
     */
    private String channelMiddleImage;

    /**
     * 6-频道页预告图-310×200
     */
    private String channelComingSoon;

    /**
     * 7-手机焦点图-1520x622
     */
    private String mobileFocus;

    /**
     * PC端——专场头图-1920x840
     */
    private String pcHeader;

    /**
     * 移动端——专场头图-640x790
     */
    private String appHeader;

    /**
     * phone正式,app频道页专场入口图(目前使用在首页)-2048x847
     */
    private String appChannelEntrance;

    /**
     * 1-app弧形轮播-2048X1142
     */
    private String appArcCarousel;

    /**
     * 2-pc轮播-1920X350
     */
    private String pcCarousel;

    /**
     * 3-PC一排2个-472x170
     */
    private String pcRow1Cell2;

    /**
     * 4-app大卡片位右-2048X1042
     */
    private String appRightBigCard;

    /**
     * 4-app大卡片位左-2048X1042
     */
    private String appLeftBigCard;

    /**
     * 4-app小卡片位-1024x1024
     */
    private String appSmallCard;

    /**
     * 7-PC首页预热-535X212
     */
    private String pcIndexPreheat;

    /**
     * 7-PC首页正式-535X212
     */
    private String pcIndex;

    /**
     * 8-ipad轮播-2048x512
     */
    private String padCarousel;

    /**
     * 9-ipad卡片位模板预热-732x244
     */
    private String padPreheat;

    /**
     * 9-ipad卡片位模板正式-732x244
     */
    private String padCard;

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getPcEntrance() {
        return pcEntrance;
    }

    public void setPcEntrance(String pcEntrance) {
        this.pcEntrance = pcEntrance;
    }

    public String getAppEntrance() {
        return appEntrance;
    }

    public void setAppEntrance(String appEntrance) {
        this.appEntrance = appEntrance;
    }

    public String getWxShare() {
        return wxShare;
    }

    public void setWxShare(String wxShare) {
        this.wxShare = wxShare;
    }

    public String getChannelFrontCover() {
        return channelFrontCover;
    }

    public void setChannelFrontCover(String channelFrontCover) {
        this.channelFrontCover = channelFrontCover;
    }

    public String getChannelMiddleImage() {
        return channelMiddleImage;
    }

    public void setChannelMiddleImage(String channelMiddleImage) {
        this.channelMiddleImage = channelMiddleImage;
    }

    public String getChannelComingSoon() {
        return channelComingSoon;
    }

    public void setChannelComingSoon(String channelComingSoon) {
        this.channelComingSoon = channelComingSoon;
    }

    public String getMobileFocus() {
        return mobileFocus;
    }

    public void setMobileFocus(String mobileFocus) {
        this.mobileFocus = mobileFocus;
    }

    public String getPcHeader() {
        return pcHeader;
    }

    public void setPcHeader(String pcHeader) {
        this.pcHeader = pcHeader;
    }

    public String getAppHeader() {
        return appHeader;
    }

    public void setAppHeader(String appHeader) {
        this.appHeader = appHeader;
    }

    public String getAppChannelEntrance() {
        return appChannelEntrance;
    }

    public void setAppChannelEntrance(String appChannelEntrance) {
        this.appChannelEntrance = appChannelEntrance;
    }

    public String getAppArcCarousel() {
        return appArcCarousel;
    }

    public void setAppArcCarousel(String appArcCarousel) {
        this.appArcCarousel = appArcCarousel;
    }

    public String getPcCarousel() {
        return pcCarousel;
    }

    public void setPcCarousel(String pcCarousel) {
        this.pcCarousel = pcCarousel;
    }

    public String getPcRow1Cell2() {
        return pcRow1Cell2;
    }

    public void setPcRow1Cell2(String pcRow1Cell2) {
        this.pcRow1Cell2 = pcRow1Cell2;
    }

    public String getAppRightBigCard() {
        return appRightBigCard;
    }

    public void setAppRightBigCard(String appRightBigCard) {
        this.appRightBigCard = appRightBigCard;
    }

    public String getAppLeftBigCard() {
        return appLeftBigCard;
    }

    public void setAppLeftBigCard(String appLeftBigCard) {
        this.appLeftBigCard = appLeftBigCard;
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

    public String getPadPreheat() {
        return padPreheat;
    }

    public void setPadPreheat(String padPreheat) {
        this.padPreheat = padPreheat;
    }

    public String getPadCard() {
        return padCard;
    }

    public void setPadCard(String padCard) {
        this.padCard = padCard;
    }
}
