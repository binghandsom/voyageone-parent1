package com.voyageone.web2.cms.bean.search.index;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 16/1/8
 */
public class SearchGroupsBean {

    private CmsBtProductModel group;

    private List<Long> productIds;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public CmsBtProductModel getGroup() {
        return group;
    }

    public void setGroup(CmsBtProductModel group) {
        this.group = group;
    }
}
