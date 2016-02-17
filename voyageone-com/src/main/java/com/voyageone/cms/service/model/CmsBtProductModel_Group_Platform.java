package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.cms.CmsConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 的商品Model Group>Platform
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Group_Platform extends BaseMongoMap<String, Object> {

    public CmsBtProductModel_Group_Platform() {

    }
    public CmsBtProductModel_Group_Platform(Map m) {
        this.putAll(m);
    }

    public Long getGroupId() {
        Long result = null;
        Object groupIdObj = getAttribute("groupId");
        if (groupIdObj != null) {
            if (groupIdObj instanceof Long) {
                result = (Long)groupIdObj;
            } else {
                result = Long.valueOf(groupIdObj.toString());
            }
        }
        return result;
    }

    public void setGroupId(Long groupId) {
        setAttribute("groupId", groupId);
    }

    public Integer getCartId() {
        return getAttribute("cartId");
    }

    public void setCartId(Integer cartId) {
        setAttribute("cartId", cartId);
    }

    public String getNumIId() {
        return getAttribute("numIId");
    }

    public void setNumIId(String numIId) {
        setAttribute("numIId", numIId);
    }

    public String getProductId() {
        return getAttribute("productId");
    }

    public void setProductId(String productId) {
        setAttribute("productId", productId);
    }

    public boolean getIsMain() {
        boolean result = false;
        Integer isMain = getAttribute("isMain");
        if (isMain != null && isMain == 1) {
            result = true;
        }
        return result;
    }

    public void setIsMain(boolean isMain) {
        int value = 0;
        if (isMain) {
            value = 1;
        }
        setAttribute("isMain", value);
    }

    public Integer getDisplayOrder() {
        return getAttribute("displayOrder");
    }

    public void setDisplayOrder(Integer displayOrder) {
        setAttribute("displayOrder", displayOrder);
    }

    public String getPublishTime() {
        return getAttribute("publishTime");
    }

    public void setPublishTime(String publishTime) {
        setAttribute("publishTime", publishTime);
    }

    public String getOnSaleTime() {
        return getAttribute("onSaleTime");
    }

    public void setOnSaleTime(String onSaleTime) {
        setAttribute("onSaleTime", onSaleTime);
    }

    public String getInstockTime() {
        return getAttribute("instockTime");
    }

    public void setInstockTime(String instockTime) {
        setAttribute("instockTime", instockTime);
    }

    // platform status 等待上新/在售/在库
    public CmsConstants.PlatformStatus getPlatformStatus() {
        String platformStatus = getAttribute("platformStatus");
        return (platformStatus == null)?null:CmsConstants.PlatformStatus.valueOf(platformStatus);
    }

    public void setPlatformStatus(CmsConstants.PlatformStatus platformStatus) {
        setAttribute("platformStatus", platformStatus);
    }

    //"Instock"(在库)/"OnSale"(在售)
    public CmsConstants.PlatformActive getPlatformActive() {
        String platformActive = getAttribute("platformActive").toString();
        return (platformActive == null)?null:CmsConstants.PlatformActive.valueOf(platformActive);
    }
    public void setPlatformActive(CmsConstants.PlatformActive platformActive) {
        setAttribute("platformActive", platformActive);
    }


    public Integer getQty() {
        return getAttribute("qty");
    }

    public void setQty(Integer qty) {
        setAttribute("qty", qty);
    }

}