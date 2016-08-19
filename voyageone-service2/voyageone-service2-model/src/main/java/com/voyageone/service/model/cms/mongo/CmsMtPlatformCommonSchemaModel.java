package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;
import java.util.Map;

/**
 * cms_mt_platform_common_schema 表的模型
 * <p>
 * Created by jonas on 8/17/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class CmsMtPlatformCommonSchemaModel extends BaseMongoModel {

    private Integer cartId;

    private List<Map<String, Object>> propsProduct;

    private List<Map<String, Object>> propsItem;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<Map<String, Object>> getPropsProduct() {
        return propsProduct;
    }

    public void setPropsProduct(List<Map<String, Object>> propsProduct) {
        this.propsProduct = propsProduct;
    }

    public List<Map<String, Object>> getPropsItem() {
        return propsItem;
    }

    public void setPropsItem(List<Map<String, Object>> propsItem) {
        this.propsItem = propsItem;
    }
}
