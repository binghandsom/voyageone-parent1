/*
 * CmsBtJmSkuModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.math.BigDecimal;

/**
 * JMBTPromotionSku|| 聚美推广活动商品规格表
 */
public class CmsBtJmSkuModel extends BaseModel {
    /**
     * 渠道id
     */
    protected String channelId;

    /**
     * 商品Code
     */
    protected String productCode;

    /**
     * 品牌方SKU(聚美商家商品编码)
     */
    protected String skuCode;

    /**
     * 聚美子产品ID
     */
    protected String jmSpuNo;

    /**
     * 聚美SKU
     */
    protected String jmSkuNo;

    /**
     * 规格  子规格
     */
    protected String format;

    /**
     * 尺码(VO系统)
     */
    protected String cmsSize;

    /**
     * 尺码（聚美系统）
     */
    protected String jmSize;

    /**
     * 海外官网价格
     */
    protected BigDecimal msrp;

    /**
     * 中国最终售价
     */
    protected BigDecimal salePrice;

    /**
     * 中国指导价格
     */
    protected BigDecimal retailPrice;

    /**
     * 商品条形码
     */
    protected String upc;

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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public String getJmSpuNo() {
        return jmSpuNo;
    }

    public void setJmSpuNo(String jmSpuNo) {
        this.jmSpuNo = jmSpuNo == null ? null : jmSpuNo.trim();
    }

    public String getJmSkuNo() {
        return jmSkuNo;
    }

    public void setJmSkuNo(String jmSkuNo) {
        this.jmSkuNo = jmSkuNo == null ? null : jmSkuNo.trim();
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }

    public String getCmsSize() {
        return cmsSize;
    }

    public void setCmsSize(String cmsSize) {
        this.cmsSize = cmsSize == null ? null : cmsSize.trim();
    }

    public String getJmSize() {
        return jmSize;
    }

    public void setJmSize(String jmSize) {
        this.jmSize = jmSize == null ? null : jmSize.trim();
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc == null ? null : upc.trim();
    }
}