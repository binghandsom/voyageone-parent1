package com.voyageone.service.model.ims;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by morse.lu on 16/4/20.
 */
public class ImsBtProductModel extends BaseModel {
    private Long seq;
    private String channelId;
    private int cartId;
    private int productId;
    private int modelId;

    private String code;
    private String numIid;
    private String productCode;
    private int mainProductFlg;
    private int productType;

    private String quantityUpdateType;
    private int cnGroupId;
    private String isPublished;
    private String publishProductId;
    private String publishProductStatus;

    private String prePublishDatetime;
    private String publishDatetime;
    private String publishSkip;
    private String publishStatus;
    private String publishFaildComment;

    private String publishPromotionPriceStatus;
    private String promotionFaildComment;

    private String orgChannelId;


    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getMainProductFlg() {
        return mainProductFlg;
    }

    public void setMainProductFlg(int mainProductFlg) {
        this.mainProductFlg = mainProductFlg;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getQuantityUpdateType() {
        return quantityUpdateType;
    }

    public void setQuantityUpdateType(String quantityUpdateType) {
        this.quantityUpdateType = quantityUpdateType;
    }

    public int getCnGroupId() {
        return cnGroupId;
    }

    public void setCnGroupId(int cnGroupId) {
        this.cnGroupId = cnGroupId;
    }

    public String getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(String isPublished) {
        this.isPublished = isPublished;
    }

    public String getPublishProductId() {
        return publishProductId;
    }

    public void setPublishProductId(String publishProductId) {
        this.publishProductId = publishProductId;
    }

    public String getPublishProductStatus() {
        return publishProductStatus;
    }

    public void setPublishProductStatus(String publishProductStatus) {
        this.publishProductStatus = publishProductStatus;
    }

    public String getPrePublishDatetime() {
        return prePublishDatetime;
    }

    public void setPrePublishDatetime(String prePublishDatetime) {
        this.prePublishDatetime = prePublishDatetime;
    }

    public String getPublishDatetime() {
        return publishDatetime;
    }

    public void setPublishDatetime(String publishDatetime) {
        this.publishDatetime = publishDatetime;
    }

    public String getPublishSkip() {
        return publishSkip;
    }

    public void setPublishSkip(String publishSkip) {
        this.publishSkip = publishSkip;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getPublishFaildComment() {
        return publishFaildComment;
    }

    public void setPublishFaildComment(String publishFaildComment) {
        this.publishFaildComment = publishFaildComment;
    }

    public String getPublishPromotionPriceStatus() {
        return publishPromotionPriceStatus;
    }

    public void setPublishPromotionPriceStatus(String publishPromotionPriceStatus) {
        this.publishPromotionPriceStatus = publishPromotionPriceStatus;
    }

    public String getPromotionFaildComment() {
        return promotionFaildComment;
    }

    public void setPromotionFaildComment(String promotionFaildComment) {
        this.promotionFaildComment = promotionFaildComment;
    }

    public String getOrgChannelId() {
        return orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId;
    }

}
