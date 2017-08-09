package com.voyageone.components.cnn.request.bean;

import java.util.List;

/**
 * 商品信息Bean
 * Created by morse on 2017/7/31.
 */
public class CategoryInfoBean {

    private String catId; // 店铺内分类ID
    private String catName; // 店铺内分类名称
    private List<CategoryInfoBean> children; // 子节点列表

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

    public List<CategoryInfoBean> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryInfoBean> children) {
        this.children = children;
    }
}
