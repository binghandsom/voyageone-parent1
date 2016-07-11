package com.voyageone.service.bean.cms.jumei;

import java.util.List;

/**
 * Created by dell on 2016/5/31.
 */
public class BatchDeleteProductParameter {
    List<Long> listPromotionProductId;
    int promotionId;
    List<String> listProductCode;
    public List<String> getListProductCode() {
        return listProductCode;
    }

    public void setListProductCode(List<String> listProductCode) {
        this.listProductCode = listProductCode;
    }




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
