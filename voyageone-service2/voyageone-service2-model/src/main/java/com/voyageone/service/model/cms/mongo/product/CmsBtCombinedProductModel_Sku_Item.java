package com.voyageone.service.model.cms.mongo.product;

/**
 * Created by rex.wu on 2016/11/29.
 */
public class CmsBtCombinedProductModel_Sku_Item implements Cloneable {

    private String code; // 记录商品code
    private String skuCode; // 真实SKU code
    private Double sellingPriceCn; // 单品中国最终售价
    private Double preferentialPrice;  // 单品优惠售价
    private String productName; // 商品名称

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

}
