package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.Map;

/**
 * 的商品Model Group>Platform
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Group_Platform extends BaseMongoMap<Object, Object> {

    public CmsBtProductModel_Group_Platform() {

    }
    public CmsBtProductModel_Group_Platform(Map m) {
        this.putAll(m);
    }

    public Integer getGroupId() {
        return getAttribute("groupId");
    }

    public void setGroupId(int groupId) {
        setAttribute("groupId", groupId);
    }

    public int getCartId() {
        return getAttribute("cartId");
    }

    public void setCartId(int cartId) {
        setAttribute("cartId", cartId);
    }

    public String getNumIId() {
        return getAttribute("numIId");
    }

    public void setNumIId(String numIId) {
        setAttribute("numIId", numIId);
    }

    public String getProductId() {
        return getAttribute("productId");
    }

    public void setProductId(String productId) {
        setAttribute("productId", productId);
    }

    public boolean getIsMain() {
        boolean result = false;
        Integer isMain = getAttribute("isMain");
        if (isMain != null && isMain == 1) {
            result = true;
        }
        return result;
    }

    public void setIsMain(boolean isMain) {
        int value = 0;
        if (isMain) {
            value = 1;
        }
        setAttribute("isMain", value);
    }

    public double getDisplayOrder() {
        return getAttribute("displayOrder");
    }

    public void setDisplayOrder(int displayOrder) {
        setAttribute("displayOrder", displayOrder);
    }

    public String getPublishTime() {
        return getAttribute("publishTime");
    }

    public void setPublishTime(String publishTime) {
        setAttribute("publishTime", publishTime);
    }

    public String getInstockTime() {
        return getAttribute("instockTime");
    }

    public void setInstockTime(String instockTime) {
        setAttribute("instockTime", instockTime);
    }

    public String getStatus() {
        return getAttribute("status");
    }

    public void setStatus(String status) {
        setAttribute("status", status);
    }

    public String getPublishStatus() {
        return getAttribute("publishStatus");
    }

    public void setPublishStatus(String publishStatus) {
        setAttribute("publishStatus", publishStatus);
    }

    public String getComment() {
        return getAttribute("comment");
    }

    public void setComment(String comment) {
        setAttribute("comment", comment);
    }

    public int getInventory() {
        return getAttribute("inventory");
    }

    public void setInventory(int inventory) {
        setAttribute("inventory", inventory);
    }

}