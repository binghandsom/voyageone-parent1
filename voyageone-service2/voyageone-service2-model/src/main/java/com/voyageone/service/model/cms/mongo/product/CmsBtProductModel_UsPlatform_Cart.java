package com.voyageone.service.model.cms.mongo.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;

import java.util.List;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
public class CmsBtProductModel_UsPlatform_Cart extends BaseMongoMap<String, Object> {
    //cartId
    public Integer getCartId() {
        return getIntAttribute("cartId");
    }
    public void setCartId(Integer cartId) {
        setAttribute("cartId", cartId);
    }
    public final static String SKUS = "skus";

    //skus
    public List<BaseMongoMap<String, Object>> getSkus() {
        return getAttribute(SKUS);
    }
    public void setSkus(List<BaseMongoMap<String, Object>> skus) {
        setAttribute(SKUS, skus);
    }

    //

    //status
    public String getStatus() {
        return getStringAttribute("status");
    }
    public void setStatus(String status) {
        setStringAttribute("status", status);
    }
    public void setStatus(CmsConstants.ProductStatus status) {
        String value = null;
        if (status != null) {
            value = status.name();
        }
        setAttribute("status", value);
    }
}
