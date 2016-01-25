package com.voyageone.batch.cms.model;

import java.util.Date;

public class JmBtSkuImportModel {
    private Integer seq;

    private String channelId;

    private String productCode;

    private String sku;

    private String upcCode;

    private Double abroadPrice;

    private Double dealPrice;

    private Double marketPrice;

    private String size;

    private String attribute;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getUpcCode() {
        return upcCode;
    }

    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode == null ? null : upcCode.trim();
    }

    public Double getAbroadPrice() {
        return abroadPrice;
    }

    public void setAbroadPrice(Double abroadPrice) {
        this.abroadPrice = abroadPrice;
    }

    public Double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute == null ? null : attribute.trim();
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