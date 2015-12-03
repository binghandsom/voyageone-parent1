package com.voyageone.cms.modelbean;

import java.math.BigDecimal;
import java.util.Date;

public class HistoryProductPrice {
    private Integer id;

    private Integer productId;

    private String channelId;

    private String cartId;

    private BigDecimal price;

    private BigDecimal effectivePrice;

    private BigDecimal cnPriceRmb;

    private BigDecimal cnPriceAdjustmentRmb;

    private BigDecimal cnPriceFinalRmb;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId == null ? null : cartId.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getEffectivePrice() {
        return effectivePrice;
    }

    public void setEffectivePrice(BigDecimal effectivePrice) {
        this.effectivePrice = effectivePrice;
    }

    public BigDecimal getCnPriceRmb() {
        return cnPriceRmb;
    }

    public void setCnPriceRmb(BigDecimal cnPriceRmb) {
        this.cnPriceRmb = cnPriceRmb;
    }

    public BigDecimal getCnPriceAdjustmentRmb() {
        return cnPriceAdjustmentRmb;
    }

    public void setCnPriceAdjustmentRmb(BigDecimal cnPriceAdjustmentRmb) {
        this.cnPriceAdjustmentRmb = cnPriceAdjustmentRmb;
    }

    public BigDecimal getCnPriceFinalRmb() {
        return cnPriceFinalRmb;
    }

    public void setCnPriceFinalRmb(BigDecimal cnPriceFinalRmb) {
        this.cnPriceFinalRmb = cnPriceFinalRmb;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}