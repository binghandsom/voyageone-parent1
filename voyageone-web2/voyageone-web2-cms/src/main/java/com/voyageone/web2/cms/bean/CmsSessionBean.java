package com.voyageone.web2.cms.bean;

import com.voyageone.web2.cms.CmsConstants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/3
 */
public class CmsSessionBean implements Serializable {

    // categoryType
    private Map<String, Object> categoryType;

    public Map<String, Object> getCategoryType() {
        if (categoryType != null) {
            return categoryType;
        } else {
            Map<String, Object> newCategoryType = new HashMap<String, Object>();
            newCategoryType.put("cTypeId", CmsConstants.DEFAULT_CATEGORY_TYPE);
            newCategoryType.put("cartId", CmsConstants.DEFAULT_CART_ID);
            return newCategoryType;
        }
    }

    public void setCategoryType(Map<String, Object> categoryType) {
        this.categoryType = categoryType;
    }
}
