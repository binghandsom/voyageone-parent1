package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtPromotionSkusModel extends BaseModel {

    /**

     */
    private int promotionId;
    /**

     */
    private String orgChannelId;
    /**

     */
    private int modelId;
    /**

     */
    private int productId;
    /**

     */
    private String numIid;
    /**

     */
    private String productModel;
    /**

     */
    private String productCode;
    /**

     */
    private String productSku;
    /**

     */
    private String catPath;
    /**

     */
    private int qty;
    /**

     */
    private String synFlg;
    /**

     */
    private String errMsg;

    /**

     */
    public int getPromotionId() {

        return this.promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }


    /**

     */
    public String getOrgChannelId() {

        return this.orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        if (orgChannelId != null) {
            this.orgChannelId = orgChannelId;
        } else {
            this.orgChannelId = "";
        }

    }


    /**

     */
    public int getModelId() {

        return this.modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
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
    public String getNumIid() {

        return this.numIid;
    }

    public void setNumIid(String numIid) {
        if (numIid != null) {
            this.numIid = numIid;
        } else {
            this.numIid = "";
        }

    }


    /**

     */
    public String getProductModel() {

        return this.productModel;
    }

    public void setProductModel(String productModel) {
        if (productModel != null) {
            this.productModel = productModel;
        } else {
            this.productModel = "";
        }

    }


    /**

     */
    public String getProductCode() {

        return this.productCode;
    }

    public void setProductCode(String productCode) {
        if (productCode != null) {
            this.productCode = productCode;
        } else {
            this.productCode = "";
        }

    }


    /**

     */
    public String getProductSku() {

        return this.productSku;
    }

    public void setProductSku(String productSku) {
        if (productSku != null) {
            this.productSku = productSku;
        } else {
            this.productSku = "";
        }

    }


    /**

     */
    public String getCatPath() {

        return this.catPath;
    }

    public void setCatPath(String catPath) {
        if (catPath != null) {
            this.catPath = catPath;
        } else {
            this.catPath = "";
        }

    }


    /**

     */
    public int getQty() {

        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }


    /**

     */
    public String getSynFlg() {

        return this.synFlg;
    }

    public void setSynFlg(String synFlg) {
        if (synFlg != null) {
            this.synFlg = synFlg;
        } else {
            this.synFlg = "";
        }

    }


    /**

     */
    public String getErrMsg() {

        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        if (errMsg != null) {
            this.errMsg = errMsg;
        } else {
            this.errMsg = "";
        }

    }

}