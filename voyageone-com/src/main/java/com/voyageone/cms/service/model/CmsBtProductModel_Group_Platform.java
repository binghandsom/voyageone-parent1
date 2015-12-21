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

    public String getInstockTime() {
        return getAttribute("instockTime");
    }

    public void setInstockTime(String instockTime) {
        setAttribute("instockTime", instockTime);
    }

    // platform status
    public CmsConstants.PlatformStatus getPlatformStatus() {
        return getAttribute("platformStatus");
    }

    public void setPlatformStatus(String platformStatus) {
        setAttribute("platformStatus", platformStatus);
    }
    public void setPlatformStatus(CmsConstants.PlatformStatus platformStatus) {
        setAttribute("platformStatus", platformStatus);
    }

    public List<Map<String,Object>> getTaskResults() {
        if (!this.containsKey("taskResults") || getAttribute("taskResults") == null) {
            setAttribute("taskResults", new ArrayList<Map<String,Object>>());
        }
        return getAttribute("taskResults");
    }

    public void setTaskResults(List<Map<String,Object>> taskResults) {
        setAttribute("taskResults", taskResults);
    }


    public Integer getInventory() {
        return getAttribute("inventory");
    }

    public void setInventory(Integer inventory) {
        setAttribute("inventory", inventory);
    }

}