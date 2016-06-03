package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;

import java.util.Map;

/**
 * 的商品Model Field>Image
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Carts extends BaseMongoMap<String, Object> {

    public CmsBtProductModel_Carts() {
    }

    public CmsBtProductModel_Carts(Map map) {
        this.putAll(map);
    }

    // platform status 等待上新/在售/在库
    public CmsConstants.PlatformStatus getPlatformStatus() {
        String platformStatus = getStringAttribute("platformStatus");
        return (platformStatus == null) ? null : CmsConstants.PlatformStatus.valueOf(platformStatus);
    }

    public void setPlatformStatus(CmsConstants.PlatformStatus platformStatus) {
        setStringAttribute("platformStatus", platformStatus);
    }

    public String getPublishTime() {
        return getAttribute("publishTime");
    }

    public void setPublishTime(String publishTime) {
        setAttribute("publishTime", publishTime);
    }

    public Integer getCartId() {
        Object cart = getAttribute("cartId");
        if (cart == null) {
            cart = 0;
        }
        return ((Number) cart).intValue();
    }

    public void setCartId(Integer cartId) {
        setAttribute("cartId", cartId);
    }

}