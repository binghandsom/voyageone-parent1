package com.voyageone.service.model.cms.mongo.product;

import java.util.Date;

/**
 * Created by yangjindong on 2017/6/21.
 */
public class CmsBtPriceLogFlatModel {
    private String channelId;
    private long productId;
    // cart_id
    private int cartId;
    private String code;
    private String sku;
    private Double msrpPrice;
    private Double retailPrice;
    private Double salePrice;
    private Double clientMsrpPrice;
    private Double clientRetailPrice;
    private Double clientNetPrice;
    private String comment;
    private Date created;
    private String creater;
    private Date modified;
    private String modifier;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getMsrpPrice() {
        return msrpPrice;
    }

    public void setMsrpPrice(Double msrpPrice) {
        this.msrpPrice = msrpPrice;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getClientMsrpPrice() {
        return clientMsrpPrice;
    }

    public void setClientMsrpPrice(Double clientMsrpPrice) {
        this.clientMsrpPrice = clientMsrpPrice;
    }

    public Double getClientRetailPrice() {
        return clientRetailPrice;
    }

    public void setClientRetailPrice(Double clientRetailPrice) {
        this.clientRetailPrice = clientRetailPrice;
    }

    public Double getClientNetPrice() {
        return clientNetPrice;
    }

    public void setClientNetPrice(Double clientNetPrice) {
        this.clientNetPrice = clientNetPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        this.creater = creater;
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
        this.modifier = modifier;
    }
}
