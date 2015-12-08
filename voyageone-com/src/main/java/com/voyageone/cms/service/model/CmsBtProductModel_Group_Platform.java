package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.Map;

public class CmsBtProductModel_Group_Platform extends BaseMongoMap {

    public CmsBtProductModel_Group_Platform() {

    }
    public CmsBtProductModel_Group_Platform(Map m) {
        this.putAll(m);
    }

    public int getGroupId() {
        return (int) getAttribute("groupId");
    }

    public void setGroupId(int groupId) {
        setAttribute("groupId", groupId);
    }

    public double getCartId() {
        return (int) getAttribute("cartId");
    }

    public void setCartId(int cartId) {
        setAttribute("cartId", cartId);
    }

    public String getNumIId() {
        return (String) getAttribute("numIId");
    }

    public void setNumIId(String numIId) {
        setAttribute("numIId", numIId);
    }

    public String getProductId() {
        return (String) getAttribute("productId");
    }

    public void setProductId(String productId) {
        setAttribute("productId", productId);
    }

    public boolean isMain() {
        return (Boolean) getAttribute("isMain");
    }

    public void setIsMain(boolean isMain) {
        setAttribute("isMain", isMain);
    }

    public double getDisplayOrder() {
        return (int) getAttribute("displayOrder");
    }

    public void setDisplayOrder(int displayOrder) {
        setAttribute("displayOrder", displayOrder);
    }

    public String getPublishTime() {
        return (String) getAttribute("publishTime");
    }

    public void setPublishTime(String publishTime) {
        setAttribute("publishTime", publishTime);
    }

    public String getInstockTime() {
        return (String) getAttribute("instockTime");
    }

    public void setInstockTime(String instockTime) {
        setAttribute("instockTime", instockTime);
    }

    public String getStatus() {
        return (String) getAttribute("status");
    }

    public void setStatus(String status) {
        setAttribute("status", status);
    }

    public String getPublishStatus() {
        return (String) getAttribute("publishStatus");
    }

    public void setPublishStatus(String publishStatus) {
        setAttribute("publishStatus", publishStatus);
    }

    public String getComment() {
        return (String) getAttribute("comment");
    }

    public void setComment(String comment) {
        setAttribute("comment", comment);
    }

    public int getInventory() {
        return (int) getAttribute("inventory");
    }

    public void setInventory(int inventory) {
        setAttribute("inventory", inventory);
    }

}