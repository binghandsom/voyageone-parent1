package com.voyageone.service.bean.cms.jumei2;

import java.util.List;

/**
 * Created by dell on 2016/5/31.
 */
public class BatchDeleteProductParameter {
    List<Long> listPromotionProductId;
    int promotionId;
    public int getPromotionId() {
        return promotionId;
    }
    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public List<Long> getListPromotionProductId() {
        return listPromotionProductId;
    }

    public void setListPromotionProductId(List<Long> listPromotionProductId) {
        this.listPromotionProductId = listPromotionProductId;
    }
}
