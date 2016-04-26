package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtPromotionCodesModel extends BaseModel {

    /**

     */
    private int promotionId;
    /**

     */
    private String orgChannelId;
    /**

     */
    private int productId;
    /**

     */
    private int modelId;
    /**

     */
    private String productModel;
    /**

     */
    private String productCode;
    /**

     */
    private String productName;
    /**

     */
    private String catPath;
    /**

     */
    private double retailPrice;
    /**

     */
    private double salePrice;
    /**

     */
    private double msrp;
    /**

     */
    private double msrpUS;
    /**

     */
    private int tagId;
    /**

     */
    private double promotionPrice;
    /**

     */
    private String numIid;
    /**
     * 图片1
     */
    private String imageUrl1;
    /**
     * 图片2
     */
    private String imageUrl2;
    /**
     * 竖图1
     */
    private String imageUrl3;
    /**

     */
    private String property1;
    /**

     */
    private String property2;
    /**

     */
    private String property3;
    /**

     */
    private String property4;
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
    public int getProductId() {

        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
    public String getProductName() {

        return this.productName;
    }

    public void setProductName(String productName) {
        if (productName != null) {
            this.productName = productName;
        } else {
            this.productName = "";
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
    public double getRetailPrice() {

        return this.retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }


    /**

     */
    public double getSalePrice() {

        return this.salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }


    /**

     */
    public double getMsrp() {

        return this.msrp;
    }

    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }


    /**

     */
    public double getMsrpUS() {

        return this.msrpUS;
    }

    public void setMsrpUS(double msrpUS) {
        this.msrpUS = msrpUS;
    }


    /**

     */
    public int getTagId() {

        return this.tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }


    /**

     */
    public double getPromotionPrice() {

        return this.promotionPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        this.promotionPrice = promotionPrice;
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
     * 图片1
     */
    public String getImageUrl1() {

        return this.imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        if (imageUrl1 != null) {
            this.imageUrl1 = imageUrl1;
        } else {
            this.imageUrl1 = "";
        }

    }


    /**
     * 图片2
     */
    public String getImageUrl2() {

        return this.imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        if (imageUrl2 != null) {
            this.imageUrl2 = imageUrl2;
        } else {
            this.imageUrl2 = "";
        }

    }


    /**
     * 竖图1
     */
    public String getImageUrl3() {

        return this.imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        if (imageUrl3 != null) {
            this.imageUrl3 = imageUrl3;
        } else {
            this.imageUrl3 = "";
        }

    }


    /**

     */
    public String getProperty1() {

        return this.property1;
    }

    public void setProperty1(String property1) {
        if (property1 != null) {
            this.property1 = property1;
        } else {
            this.property1 = "";
        }

    }


    /**

     */
    public String getProperty2() {

        return this.property2;
    }

    public void setProperty2(String property2) {
        if (property2 != null) {
            this.property2 = property2;
        } else {
            this.property2 = "";
        }

    }


    /**

     */
    public String getProperty3() {

        return this.property3;
    }

    public void setProperty3(String property3) {
        if (property3 != null) {
            this.property3 = property3;
        } else {
            this.property3 = "";
        }

    }


    /**

     */
    public String getProperty4() {

        return this.property4;
    }

    public void setProperty4(String property4) {
        if (property4 != null) {
            this.property4 = property4;
        } else {
            this.property4 = "";
        }

    }
}