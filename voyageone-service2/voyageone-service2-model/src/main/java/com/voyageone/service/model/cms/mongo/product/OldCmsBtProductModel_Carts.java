package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;

import java.util.Map;

/**
 * CmsBtProductModel_Carts
 *
 * @author jiangjusheng, 2016/04/22
 * @version 2.0.0
 * @since 2.0.0
 */
public class OldCmsBtProductModel_Carts extends BaseMongoMap<String, Object> {

    public OldCmsBtProductModel_Carts() {
    }

    public OldCmsBtProductModel_Carts(Map map) {
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
        return getIntAttribute("cartId");
    }

    public void setCartId(Integer cartId) {
        setAttribute("cartId", cartId);
    }

    public String getNumIid() {
        return getStringAttribute("numIId");
    }

    public void setNumIid(String numIid) {
        setStringAttribute("numIId", numIid);
    }

}