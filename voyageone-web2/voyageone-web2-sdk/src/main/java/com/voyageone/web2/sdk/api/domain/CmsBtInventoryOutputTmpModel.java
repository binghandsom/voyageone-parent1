package com.voyageone.web2.sdk.api.domain;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * @author jerry 15/12/23
 * @version 2.0.0
 */
public class CmsBtInventoryOutputTmpModel extends BaseModel {
    private String orderChannelId;
    private String prodId;
    private String groupId;
    private String numIid;
    private String model;
    private String code;
    private String skuCode;
    private String productName;
    private String priceSale;
    private String qtyOrgin;

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(String priceSale) {
        this.priceSale = priceSale;
    }

    public String getQtyOrgin() {
        return qtyOrgin;
    }

    public void setQtyOrgin(String qtyOrgin) {
        this.qtyOrgin = qtyOrgin;
    }
}
