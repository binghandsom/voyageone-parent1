package com.voyageone.service.model.cms.mongo.product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rex.wu on 2016/11/29.
 */
public class CmsBtCombinedProductModel_Sku {

    private String suitSkuCode; // 组合套装sku（商家编码）
    private Double suitSellingPriceCn; // 组合套装中国最终售价
    private Double suitPreferentialPrice;  // 组合套装优惠售价
    private List<CmsBtCombinedProductModel_Sku_Item> skuItems;

    public String getSuitSkuCode() {
        return suitSkuCode;
    }

    public void setSuitSkuCode(String suitSkuCode) {
        this.suitSkuCode = suitSkuCode;
    }

    public Double getSuitPreferentialPrice() {
        return suitPreferentialPrice;
    }

    public void setSuitPreferentialPrice(Double suitPreferentialPrice) {
        this.suitPreferentialPrice = suitPreferentialPrice;
    }

    public Double getSuitSellingPriceCn() {
        return suitSellingPriceCn;
    }

    public void setSuitSellingPriceCn(Double suitSellingPriceCn) {
        this.suitSellingPriceCn = suitSellingPriceCn;
    }

    public List<CmsBtCombinedProductModel_Sku_Item> getSkuItems() {
        if (skuItems == null) {
            skuItems = new ArrayList<CmsBtCombinedProductModel_Sku_Item>();
        }
        return skuItems;
    }

    public void setSkuItems(List<CmsBtCombinedProductModel_Sku_Item> skuItems) {
        this.skuItems = skuItems;
    }
}
