package com.voyageone.service.bean.cms.tools;

/**
 * 用于默认属性功能强制重刷商品属性的参数传递
 * Created by jonas on 2016/11/2.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
public class RefreshProductsBean {
    private int cartId;
    private String categoryPath;
    private int categoryType;
    private String channelId;
    private String fieldId;
    private boolean allProduct;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public boolean isAllProduct() {
        return allProduct;
    }

    public void setAllProduct(boolean allProduct) {
        this.allProduct = allProduct;
    }
}