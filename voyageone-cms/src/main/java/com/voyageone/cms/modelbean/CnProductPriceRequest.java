package com.voyageone.cms.modelbean;

import java.math.BigDecimal;
import java.util.Date;

public class CnProductPriceRequest {
    private Integer id;

    private Integer productId;

    private String channelId;

    private String cartId;

    private BigDecimal msrp;

    private BigDecimal defaultPrice;

    private BigDecimal price;

    private BigDecimal priceFinalRmb;

    private BigDecimal requestPrice;

    private BigDecimal requestPriceFinalRmb;

    private String requestStatus;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

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

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceFinalRmb() {
        return priceFinalRmb;
    }

    public void setPriceFinalRmb(BigDecimal priceFinalRmb) {
        this.priceFinalRmb = priceFinalRmb;
    }

    public BigDecimal getRequestPrice() {
        return requestPrice;
    }

    public void setRequestPrice(BigDecimal requestPrice) {
        this.requestPrice = requestPrice;
    }

    public BigDecimal getRequestPriceFinalRmb() {
        return requestPriceFinalRmb;
    }

    public void setRequestPriceFinalRmb(BigDecimal requestPriceFinalRmb) {
        this.requestPriceFinalRmb = requestPriceFinalRmb;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus == null ? null : requestStatus.trim();
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