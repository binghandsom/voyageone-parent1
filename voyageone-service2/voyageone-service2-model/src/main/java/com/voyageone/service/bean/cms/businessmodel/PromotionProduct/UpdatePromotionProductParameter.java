package com.voyageone.service.bean.cms.businessmodel.PromotionProduct;

/**
 * Created by dell on 2016/6/24.
 */
public class UpdatePromotionProductParameter {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    int id;//promotionProductId

    int limit;
}
