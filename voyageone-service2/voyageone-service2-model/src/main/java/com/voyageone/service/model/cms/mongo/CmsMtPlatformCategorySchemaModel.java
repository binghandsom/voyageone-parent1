package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.CartPartitionModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CmsMtPlatformCategorySchemaModel extends CartPartitionModel {

    private String catId;

    private String catFullPath;

    private String propsProduct;

    private String propsItem;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatFullPath() {
        return catFullPath;
    }

    public void setCatFullPath(String catFullPath) {
        this.catFullPath = catFullPath;
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

    public class CmsMtPlatformCatSchemaKeyModel {
        private Integer cartId;
        private String catId;

        public Integer getCartId() {
            return cartId;
        }

        public void setCartId(Integer cartId) {
            this.cartId = cartId;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }
    }
}