package com.voyageone.batch.ims.modelbean;


import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PlatformCategoryMongoBean extends BaseMongoModel {

    public static final String COLLECTION_NAME = "platform_category";

    private String channelId;
    private String cartId;
    private String categoryId;
    private String categoryName;
    private String categoryPath;
    private String parentId;
    private String propsProduct;
    private String propsItem;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public PlatformCategoryMongoBean() {
    }

    public PlatformCategoryMongoBean(
            String channelId,
            String categoryId,
            String categoryName,
            String categoryPath,
            String parentId,
            String propsProduct,
            String propsItem
    ) {
        this.channelId = channelId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryPath = categoryPath;
        this.parentId = parentId;
        this.propsProduct = propsProduct;
        this.propsItem = propsItem;
    }

    public static String getCollectionName() {
        return COLLECTION_NAME;
    }

}