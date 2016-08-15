package com.voyageone.web2.cms.bean.tools.product;

import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;

/**
 * Created by jonas on 8/15/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class PlatformMappingBean extends CmsBtPlatformMappingModel {

    private Integer cartId;

    private Integer categoryType;

    private String categoryPath;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }
}
