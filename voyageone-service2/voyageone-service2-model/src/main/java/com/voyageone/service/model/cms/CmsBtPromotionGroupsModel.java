package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtPromotionGroupsModel extends BaseModel {

    /**

     */
    private int promotionId;
    /**

     */
    private String orgChannelId;
    /**

     */
    private Long modelId;
    /**

     */
    private String productModel;
    /**

     */
    private String catPath;
    /**

     */
    private String numIid;
    /**

     */
    private String synFlg;
    /**

     */


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
    public Long getModelId() {

        return this.modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
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

}