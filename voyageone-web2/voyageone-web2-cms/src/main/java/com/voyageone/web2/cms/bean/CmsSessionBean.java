package com.voyageone.web2.cms.bean;

import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.CmsConstants;

import java.io.Serializable;

/**
 * @author Edward
 * @version 2.0.0, 15/12/3
 */
public class CmsSessionBean implements Serializable {

    // categoryType
    private String categoryType;

    public String getCategoryType() {
        return StringUtils.isEmpty(categoryType) ? CmsConstants.DEFAULT_CATEGORY_TYPE : categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
