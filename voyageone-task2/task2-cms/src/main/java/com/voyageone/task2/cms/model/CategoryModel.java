package com.voyageone.task2.cms.model;

/**
 * Created by zhujiaye on 15/6/28.
 */
public class CategoryModel {
    // 主数据类目id
    private int categoryId;
    // 父类目id
    private int parentCid;
    // 是否是父类目（是否包含子类目：1：包含； 0：不包含）
    private int isParent;
    // 类目名称
    private String categoryName;
    // 类目path
    private String categoryPath;
    // 显示顺序
    private int sortOrder;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getParentCid() {
        return parentCid;
    }

    public void setParentCid(int parentCid) {
        this.parentCid = parentCid;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
