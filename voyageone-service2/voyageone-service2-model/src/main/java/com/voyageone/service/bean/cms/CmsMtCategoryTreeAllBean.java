package com.voyageone.service.bean.cms;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmsMtCategoryTreeAllBean {
    private String catId;
    private String catName;
    private String catPath;
    private String parentCatId;
    private Integer isParent;
    private String singleSku;
    private List<CmsMtCategoryTreeAllBean> children = new ArrayList<>();
    private List<Map> platformCategory = new ArrayList<>();

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

    public String getSingleSku() {
        return singleSku;
    }

    public void setSingleSku(String singleSku) {
        this.singleSku = singleSku;
    }

    public List<CmsMtCategoryTreeAllBean> getChildren() {
        return children;
    }

    public void setChildren(List<CmsMtCategoryTreeAllBean> children) {
        this.children = children;
    }

    public List<Map> getPlatformCategory() {
        return platformCategory;
    }

    public void setPlatformCategory(List<Map> platformCategory) {
        this.platformCategory = platformCategory;
    }

}
