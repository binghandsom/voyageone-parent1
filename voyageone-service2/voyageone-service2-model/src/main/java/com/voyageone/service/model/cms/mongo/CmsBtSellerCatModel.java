package com.voyageone.service.model.cms.mongo;


import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
public class CmsBtSellerCatModel extends BaseMongoModel {

    private String channelId;
    private String catId;
    private String catName;
    private String catPath;
    private String parentCatId;
    private Integer isParent;
    private String fullCatId;
    private int cartId;
    private String isSpecial;
    private String urlKey;
    private Map<String, Object> mapping;

    private List<CmsBtSellerCatModel> children;

    public CmsBtSellerCatModel() {
    }

    public CmsBtSellerCatModel(String channelId, int cartId, String catId, String catName, String catPath, String parentCatId, String fullCatId, Integer isParent, List<CmsBtSellerCatModel> children) {
        this.channelId = channelId;
        this.cartId = cartId;
        this.catId = catId;
        this.catName = catName;
        this.catPath = catPath;
        this.parentCatId = parentCatId;
        this.fullCatId = fullCatId;
        this.isParent = isParent;
        this.children = children;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getParentCatId() {
        return parentCatId;
    }

    public void setParentCatId(String parentCatId) {
        this.parentCatId = parentCatId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public List<CmsBtSellerCatModel> getChildren() {
        return children;
    }

    public void setChildren(List<CmsBtSellerCatModel> children) {
        this.children = children;
    }

    public String getFullCatId() {
        return fullCatId;
    }

    public void setFullCatId(String fullCatId) {
        this.fullCatId = fullCatId;
    }

    public String getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(String isSpecial) {
        this.isSpecial = isSpecial;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public Map<String, Object> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, Object> mapping) {
        this.mapping = mapping;
    }
}