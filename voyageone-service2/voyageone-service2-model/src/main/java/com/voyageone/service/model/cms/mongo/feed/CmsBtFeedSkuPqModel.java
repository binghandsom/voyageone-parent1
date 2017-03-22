package com.voyageone.service.model.cms.mongo.feed;

import java.util.List;

/**
 * @author piao
 * @description 记录feedInfo 价格和库存
 */

public class CmsBtFeedSkuPqModel {
    private String code;
    private List<CmsBtFeedInfoModel_Sku> skus;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CmsBtFeedInfoModel_Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<CmsBtFeedInfoModel_Sku> skus) {
        this.skus = skus;
    }
}
