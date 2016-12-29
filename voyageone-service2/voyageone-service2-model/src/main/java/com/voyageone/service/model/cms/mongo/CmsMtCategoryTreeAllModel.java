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
    private Integer skuSplit;
    private String catNameEn;
    private String catPathEn;
    private String productTypeEn = "";
    private String productTypeCn = "";
    private String sizeTypeEn = "";
    private String sizeTypeCn = "";
    private String hscode8 = "";
    private String hscode10 = "";
    private String hscodeName8 = "";
    private String hscodeName10 = "";
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

    public Integer getSkuSplit() {
        return skuSplit;
    }

    public void setSkuSplit(Integer skuSplit) {
        this.skuSplit = skuSplit;
    }

    /**
     * 根据平台种类Id和平台类目名称找到对应的主类目Id
     * @param platformId 平台种类Id
     * @param platformCatPath 平台名称
     */
    public String getatIdByPlatformInfo (String platformId, String platformCatPath) {
        for (CmsMtCategoryTreeAllModel_Platform platform : platformCategory) {
            if (platform.getPlatformId().equals(platformId) && platform.getCatPath().equals(platformCatPath))
                return this.catId;
        }
        return null;
    }

    public String getCatNameEn() {
        return catNameEn;
    }

    public void setCatNameEn(String catNameEn) {
        this.catNameEn = catNameEn;
    }

    public String getCatPathEn() {
        return catPathEn;
    }

    public void setCatPathEn(String catPathEn) {
        this.catPathEn = catPathEn;
    }

    public String getProductTypeEn() {
        return productTypeEn == null?"":productTypeEn.toLowerCase();
    }

    public void setProductTypeEn(String productTypeEn) {
        this.productTypeEn = productTypeEn == null?"":productTypeEn.toLowerCase();
    }

    public String getProductTypeCn() {
        return productTypeCn;
    }

    public void setProductTypeCn(String productTypeCn) {
        this.productTypeCn = productTypeCn;
    }

    public String getSizeTypeEn() {
        return sizeTypeEn == null?"":sizeTypeEn.toLowerCase();
    }

    public void setSizeTypeEn(String sizeTypeEn) {
        this.sizeTypeEn = sizeTypeEn == null?"":sizeTypeEn.toLowerCase();
    }

    public String getSizeTypeCn() {
        return sizeTypeCn;
    }

    public void setSizeTypeCn(String sizeTypeCn) {
        this.sizeTypeCn = sizeTypeCn;
    }

    public String getHscode8() {
        return hscode8;
    }

    public void setHscode8(String hscode8) {
        this.hscode8 = hscode8;
    }

    public String getHscode10() {
        return hscode10;
    }

    public void setHscode10(String hscode10) {
        this.hscode10 = hscode10;
    }

    public String getHscodeName8() {
        return hscodeName8;
    }

    public void setHscodeName8(String hscodeName8) {
        this.hscodeName8 = hscodeName8;
    }

    public String getHscodeName10() {
        return hscodeName10;
    }

    public void setHscodeName10(String hscodeName10) {
        this.hscodeName10 = hscodeName10;
    }
}
