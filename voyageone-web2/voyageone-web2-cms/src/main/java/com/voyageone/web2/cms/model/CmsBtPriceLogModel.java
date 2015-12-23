package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mysql.BaseModel;

import java.math.BigDecimal;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
public class CmsBtPriceLogModel extends BaseModel {
    private int seq;
    private String channelId;
    private int productId;
    private String code;
    private String sku;
    private BigDecimal msrpPrice;
    private BigDecimal retailPrice;
    private BigDecimal salePrice;
    private String comment;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public BigDecimal getMsrpPrice() {
        return msrpPrice;
    }

    public void setMsrpPrice(BigDecimal msrpPrice) {
        this.msrpPrice = msrpPrice;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
