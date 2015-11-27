package com.voyageone.batch.cms.mongoModel;


import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class PlatformCategoryMongoModel extends BaseMongoModel {

    private String cartId;
    private String categoryId;
    private String categoryName;
    private String categoryPath;
    private String parentCategoryId;
    private Integer isParent;
    private List<PlatformCategoryMongoModel> subCategories;

    public PlatformCategoryMongoModel() {
    }

    public PlatformCategoryMongoModel(
            String categoryId,
            String categoryName,
            String categoryPath,
            String parentId,
            Integer isParent,
            List<PlatformCategoryMongoModel> subCategories
    ) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryPath = categoryPath;
        this.parentCategoryId = parentId;
        this.isParent = isParent;
        this.subCategories = subCategories;
    }

    /**
     * Get collection name.
     * @return
     */
    public static String getCollectionName() {
        return "platform_category";
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public List<PlatformCategoryMongoModel> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<PlatformCategoryMongoModel> subCategories) {
        this.subCategories = subCategories;
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

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }


}