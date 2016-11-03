package com.voyageone.service.bean.cms.businessmodel.CmsPromotionDetail;

import java.math.BigDecimal;

/**
 * Created by dell on 2016/11/2.
 */
public class SkuPromotionPriceInfo {
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
