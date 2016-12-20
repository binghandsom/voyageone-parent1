package com.voyageone.service.model.cms.mongo.product;

/**
 * Created by rex.wu on 2016/11/29.
 */
public class CmsBtCombinedProductModel_Sku_Item {

    private String skuCode;
    private Double sellingPriceCn; // 单品中国最终售价
    private Double preferentialPrice;  // 单品优惠售价
    private String productName; // 商品名称

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(Double preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public Double getSellingPriceCn() {
        return sellingPriceCn;
    }

    public void setSellingPriceCn(Double sellingPriceCn) {
        this.sellingPriceCn = sellingPriceCn;
    }

}
