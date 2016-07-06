package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.ArrayList;
import java.util.List;

public class CmsMtCategoryTreeAllModel extends BaseMongoModel {
    private String catId;
    private String catName;
    private String catPath;
    private String parentCatId;
    private Integer isParent;
    private String singleSku;
    private List<CmsMtCategoryTreeAllModel> children = new ArrayList<>();
    private List<CmsMtCategoryTreeAllModel_Platform> platformCategory = new ArrayList<>();

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

    public List<CmsMtCategoryTreeAllModel> getChildren() {
        return children;
    }

    public void setChildren(List<CmsMtCategoryTreeAllModel> children) {
        this.children = children;
    }

    public List<CmsMtCategoryTreeAllModel_Platform> getPlatformCategory() {
        return platformCategory;
    }

    public void setPlatformCategory(List<CmsMtCategoryTreeAllModel_Platform> platformCategory) {
        this.platformCategory = platformCategory;
    }

    /**
     * 根据平台种类Id和平台类目名称找到对应的主类目Id
     * @param platformId 平台种类Id
     * @param platformCatPath 平台名称
     * @return
     */
    public String getatIdByPlatformInfo (String platformId, String platformCatPath) {
        for (CmsMtCategoryTreeAllModel_Platform platform : platformCategory) {

            if (platform.getPlatformId().equals(platformId) && platform.getCatPath().equals(platformCatPath))
                return this.catId;
        }
        return null;
    }

    /**
     * 返回该主类目下的平台类目
     * @param platformId 平台种类Id
     * @return 平台类目
     */
    public CmsMtCategoryTreeAllModel_Platform getPlatformCategoryByPlatformId (String platformId) {
        return platformCategory.stream()
                .filter(platform -> platformId.equals(platform.getPlatformId()))
                .findFirst().get();
    }

}
