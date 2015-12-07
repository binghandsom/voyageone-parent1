package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CmsMtPlatformCategorySchemaModel extends BaseMongoModel {

    private Integer cartId;
    private String categoryId;
    private String propsProduct;
    private String propsItem;

    public CmsMtPlatformCategorySchemaModel() {
    }

    public CmsMtPlatformCategorySchemaModel(Integer cartId, String categoryId, String propsProduct, String propsItem) {
        this.cartId = cartId;
        this.categoryId = categoryId;
        this.propsProduct = propsProduct;
        this.propsItem = propsItem;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPropsProduct() {
        return propsProduct;
    }

    public void setPropsProduct(String propsProduct) {
        this.propsProduct = propsProduct;
    }

    public String getPropsItem() {
        return propsItem;
    }

    public void setPropsItem(String propsItem) {
        this.propsItem = propsItem;
    }

}