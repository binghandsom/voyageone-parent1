package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtPriceLogModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private int productId;
    /**

     */
    private String code;
    /**

     */
    private String sku;
    /**

     */
    private String msrpPrice;
    /**

     */
    private String retailPrice;
    /**

     */
    private String salePrice;
    /**

     */
    private String clientMsrpPrice;
    /**

     */
    private String clientRetailPrice;
    /**

     */
    private String clientNetPrice;
    /**

     */
    private String comment;


    /**

     */
    public String getChannelId() {

        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }

    }


    /**

     */
    public int getProductId() {

        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    /**

     */
    public String getCode() {

        return this.code;
    }

    public void setCode(String code) {
        if (code != null) {
            this.code = code;
        } else {
            this.code = "";
        }

    }


    /**

     */
    public String getSku() {

        return this.sku;
    }

    public void setSku(String sku) {
        if (sku != null) {
            this.sku = sku;
        } else {
            this.sku = "";
        }

    }


    /**

     */
    public String getMsrpPrice() {

        return this.msrpPrice;
    }

    public void setMsrpPrice(String msrpPrice) {
        if (msrpPrice != null) {
            this.msrpPrice = msrpPrice;
        } else {
            this.msrpPrice = "";
        }

    }


    /**

     */
    public String getRetailPrice() {

        return this.retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        if (retailPrice != null) {
            this.retailPrice = retailPrice;
        } else {
            this.retailPrice = "";
        }

    }


    /**

     */
    public String getSalePrice() {

        return this.salePrice;
    }

    public void setSalePrice(String salePrice) {
        if (salePrice != null) {
            this.salePrice = salePrice;
        } else {
            this.salePrice = "";
        }

    }


    /**

     */
    public String getClientMsrpPrice() {

        return this.clientMsrpPrice;
    }

    public void setClientMsrpPrice(String clientMsrpPrice) {
        if (clientMsrpPrice != null) {
            this.clientMsrpPrice = clientMsrpPrice;
        } else {
            this.clientMsrpPrice = "";
        }

    }


    /**

     */
    public String getClientRetailPrice() {

        return this.clientRetailPrice;
    }

    public void setClientRetailPrice(String clientRetailPrice) {
        if (clientRetailPrice != null) {
            this.clientRetailPrice = clientRetailPrice;
        } else {
            this.clientRetailPrice = "";
        }

    }


    /**

     */
    public String getClientNetPrice() {

        return this.clientNetPrice;
    }

    public void setClientNetPrice(String clientNetPrice) {
        if (clientNetPrice != null) {
            this.clientNetPrice = clientNetPrice;
        } else {
            this.clientNetPrice = "";
        }

    }


    /**

     */
    public String getComment() {

        return this.comment;
    }

    public void setComment(String comment) {
        if (comment != null) {
            this.comment = comment;
        } else {
            this.comment = "";
        }

    }


}