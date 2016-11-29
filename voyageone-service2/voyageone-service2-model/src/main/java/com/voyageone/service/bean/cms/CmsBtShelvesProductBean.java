package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsBtShelvesProductModel;

/**
 * Created by james on 2016/11/15.
 */
public class CmsBtShelvesProductBean extends CmsBtShelvesProductModel {
    private Double promotionPrice = 0.0;

    public Double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }
}
