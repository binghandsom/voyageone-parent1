package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

/**
 * Created by rex.wu on 2016/12/1.
 * 套装组合商品操作日志
 */
public class CmsBtCombinedProductLogModel extends BaseMongoModel {


    /*组合套装商品的ObjectId*/
    private String productId;
    private String numID;
    private String channelId;
    private Integer cartId;
    /*操作人*/
    private String operater;
    /*操作时间*/
    private String operateTime;
    /*组合套装商品状态*/
    private Integer status;
    /*组合套装商品平台状态*/
    private Integer platformStatus;
    /*操作类型*/
    private String operateType;
    /*操作内容*/
    private String operateContent;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNumID() {
        return numID;
    }

    public void setNumID(String numID) {
        this.numID = numID;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(Integer platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateContent() {
        return operateContent;
    }

    public void setOperateContent(String operateContent) {
        this.operateContent = operateContent;
    }
}
