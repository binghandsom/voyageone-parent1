package com.voyageone.web2.cms.bean;

import com.voyageone.common.masterdate.schema.field.Field;

import java.util.List;

/**
 * Created by lewis on 16-1-5.
 */
public class ProductInfoBean {

    private String channelId;

    private int productId;

    private String categoryId;

    private String categoryFullPath;

    private List<Field> masterFields;

    private Field skuFields;

    private FeedAttributesBean feedAttributes;

    public List<Field> getMasterFields() {
        return masterFields;
    }

    public void setMasterFields(List<Field> masterFields) {
        this.masterFields = masterFields;
    }

    public Field getSkuFields() {
        return skuFields;
    }

    public void setSkuFields(Field skuFields) {
        this.skuFields = skuFields;
    }

    public FeedAttributesBean getFeedAttributes() {
        return feedAttributes;
    }

    public void setFeedAttributes(FeedAttributesBean feedAttributes) {
        this.feedAttributes = feedAttributes;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryFullPath() {
        return categoryFullPath;
    }

    public void setCategoryFullPath(String categoryFullPath) {
        this.categoryFullPath = categoryFullPath;
    }
}
