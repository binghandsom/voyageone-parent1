package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
public class CmsBtPriceLogModel extends BaseModel {
    //    private Long id;
    private String channelId;
    private Long productId;
    private String code;
    private String sku;
    private String msrpPrice;
    private String retailPrice;
    private String salePrice;
    private String clientMsrpPrice;
    private String clientRetailPrice;
    private String clientNetPrice;
    private String comment;

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
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

    public String getMsrpPrice() {
        return msrpPrice;
    }

    public void setMsrpPrice(String msrpPrice) {
        this.msrpPrice = msrpPrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getClientMsrpPrice() {
        return clientMsrpPrice;
    }

    public void serClientMsrpPrice(String clientMsrpPrice) {
        this.clientMsrpPrice = clientMsrpPrice;
    }

    public String getClientRetailPrice() {
        return clientRetailPrice;
    }

    public void setClientRetailPrice(String clientRetailPrice) {
        this.clientRetailPrice = clientRetailPrice;
    }

    public String getClientNetPrice() {
        return clientNetPrice;
    }

    public void setClientNetPrice(String clientNetPrice) {
        this.clientNetPrice = clientNetPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
