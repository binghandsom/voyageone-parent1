package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

/**
 * {@link CmsBtTempProductCategoryModel} 的商品Model
 *
 * @author linanbin on 6/29/2016
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.2.0
 */
public class CmsBtTempProductCategoryModel extends ChannelPartitionModel implements Cloneable {

    private String code;
    private String catId;
    private String catName;
    private String catIdPath;
    private String catPath;
    private Integer orderSort;
    private boolean mainCategory;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCatIdPath() {
        return catIdPath;
    }

    public void setCatIdPath(String catIdPath) {
        this.catIdPath = catIdPath;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public Integer getOrderSort() {
        return orderSort;
    }

    public void setOrderSort(Integer orderSort) {
        this.orderSort = orderSort;
    }

    public boolean isMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(boolean mainCategory) {
        this.mainCategory = mainCategory;
    }

    @Override
    public CmsBtTempProductCategoryModel clone() throws CloneNotSupportedException {
        return (CmsBtTempProductCategoryModel) super.clone();
    }
}