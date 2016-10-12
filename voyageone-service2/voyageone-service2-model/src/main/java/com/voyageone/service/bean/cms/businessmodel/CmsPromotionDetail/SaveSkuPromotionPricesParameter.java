package com.voyageone.service.bean.cms.businessmodel.CmsPromotionDetail;

import java.math.BigDecimal;

/**
 * Created by dell on 2016/10/8.
 */
public class SaveSkuPromotionPricesParameter {
    public int id;
    BigDecimal promotionPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }
}
