package com.voyageone.service.bean.cms.businessmodel.CmsPromotionDetail;

import java.util.List;

/**
 * Created by dell on 2016/10/8.
 */
public class SaveSkuPromotionPricesParameter {
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    String productCode;
    int promotionId;

    List<SkuPromotionPriceInfo> listSkuPromotionPriceInfo;

    public List<SkuPromotionPriceInfo> getListSkuPromotionPriceInfo() {
        return listSkuPromotionPriceInfo;
    }

    public void setListSkuPromotionPriceInfo(List<SkuPromotionPriceInfo> listSkuPromotionPriceInfo) {
        this.listSkuPromotionPriceInfo = listSkuPromotionPriceInfo;
    }
}
