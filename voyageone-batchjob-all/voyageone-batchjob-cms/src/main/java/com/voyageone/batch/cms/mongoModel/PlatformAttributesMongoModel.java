package com.voyageone.batch.cms.mongoModel;


import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PlatformAttributesMongoModel extends BaseMongoModel {

    public static final String COLLECTION_NAME = "platform_attributes";

    private String cartId;
    private String categoryId;
    private String propsProduct;
    private String propsItem;

    public PlatformAttributesMongoModel() {
    }

    public PlatformAttributesMongoModel(
            String categoryId,
            String propsProduct,
            String propsItem
    ) {
        this.categoryId = categoryId;
        this.propsProduct = propsProduct;
        this.propsItem = propsItem;
    }

    /**
     * Get collection name.
     * @return
     */
    public static String getCollectionName() {
        return COLLECTION_NAME;
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