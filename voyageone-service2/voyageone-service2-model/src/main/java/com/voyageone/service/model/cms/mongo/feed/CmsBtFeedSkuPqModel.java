package com.voyageone.service.model.cms.mongo.feed;

import java.util.List;

/**
 * @author piao
 * @description 记录feedInfo 价格和库存
 */

public class CmsBtFeedSkuPqModel {
    private List<CmsBtFeedInfoModel_Sku> skus;

    public List<CmsBtFeedInfoModel_Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<CmsBtFeedInfoModel_Sku> skus) {
        this.skus = skus;
    }
}
