package com.voyageone.service.model.cms.mongo;


import com.voyageone.base.dao.mongodb.model.CartPartitionModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class CmsMtPlatformCategoryTreeModel extends CartPartitionModel {

    private String channelId;
    private String catId;
    private String catName;
    private String catPath;
    private String parentCatId;
    private Integer isParent;
    private List<CmsMtPlatformCategoryTreeModel> children;

    public CmsMtPlatformCategoryTreeModel() {
    }

    public CmsMtPlatformCategoryTreeModel(String channelId, int cartId, String catId, String catName, String catPath, String parentCatId, Integer isParent, List<CmsMtPlatformCategoryTreeModel> children) {
        this.channelId = channelId;
        this.cartId = cartId;
        this.catId = catId;
        this.catName = catName;
        this.catPath = catPath;
        this.parentCatId = parentCatId;
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

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public List<CmsMtPlatformCategoryTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<CmsMtPlatformCategoryTreeModel> children) {
        this.children = children;
    }

}