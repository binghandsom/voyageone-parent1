package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtBusinessLogModel extends BaseModel {

    /**

     */
    private String channelId;
    /**

     */
    private String catId;
    /**

     */
    private int cartId;
    /**

     */
    private String groupId;
    /**

     */
    private String groupName;
    /**
     * 主商品的product_id
     */
    private String productId;
    /**

     */
    private String productName;
    /**
     * 只有promotion操作error的时候才插入
     */
    private String promotionId;
    /**
     * 只有promotion操作error的时候才插入
     */
    private String promotionName;
    /**
     * 如果错误时，没有model就不要设置
     */
    private String model;
    /**
     * 如果错误时，没有code就不要设置
     */
    private String code;
    /**
     * 如果错误时，没有sku就不要设置
     */
    private String sku;
    /**
     * 1:上新错误
     */
    private int errorTypeId;
    /**

     */
    private String errorCode;
    /**
     * 1:店铺内分类 2:活动标签 3:货位标签
     */
    private String errorMsg;
    /**
     * 0:未处理 1:已处理
     */
    private int status;


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
    public String getCatId() {

        return this.catId;
    }

    public void setCatId(String catId) {
        if (catId != null) {
            this.catId = catId;
        } else {
            this.catId = "";
        }

    }


    /**

     */
    public int getCartId() {

        return this.cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }


    /**

     */
    public String getGroupId() {

        return this.groupId;
    }

    public void setGroupId(String groupId) {
        if (groupId != null) {
            this.groupId = groupId;
        } else {
            this.groupId = "";
        }

    }


    /**

     */
    public String getGroupName() {

        return this.groupName;
    }

    public void setGroupName(String groupName) {
        if (groupName != null) {
            this.groupName = groupName;
        } else {
            this.groupName = "";
        }

    }


    /**
     * 主商品的product_id
     */
    public String getProductId() {

        return this.productId;
    }

    public void setProductId(String productId) {
        if (productId != null) {
            this.productId = productId;
        } else {
            this.productId = "";
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
     * 只有promotion操作error的时候才插入
     */
    public String getPromotionId() {

        return this.promotionId;
    }

    public void setPromotionId(String promotionId) {
        if (promotionId != null) {
            this.promotionId = promotionId;
        } else {
            this.promotionId = "";
        }

    }


    /**
     * 只有promotion操作error的时候才插入
     */
    public String getPromotionName() {

        return this.promotionName;
    }

    public void setPromotionName(String promotionName) {
        if (promotionName != null) {
            this.promotionName = promotionName;
        } else {
            this.promotionName = "";
        }

    }


    /**
     * 如果错误时，没有model就不要设置
     */
    public String getModel() {

        return this.model;
    }

    public void setModel(String model) {
        if (model != null) {
            this.model = model;
        } else {
            this.model = "";
        }

    }


    /**
     * 如果错误时，没有code就不要设置
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
     * 如果错误时，没有sku就不要设置
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
     * 1:上新错误
     */
    public int getErrorTypeId() {

        return this.errorTypeId;
    }

    public void setErrorTypeId(int errorTypeId) {
        this.errorTypeId = errorTypeId;
    }


    /**

     */
    public String getErrorCode() {

        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        if (errorCode != null) {
            this.errorCode = errorCode;
        } else {
            this.errorCode = "";
        }

    }


    /**
     * 1:店铺内分类 2:活动标签 3:货位标签
     */
    public String getErrorMsg() {

        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        if (errorMsg != null) {
            this.errorMsg = errorMsg;
        } else {
            this.errorMsg = "";
        }

    }


    /**
     * 0:未处理 1:已处理
     */
    public int getStatus() {

        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}