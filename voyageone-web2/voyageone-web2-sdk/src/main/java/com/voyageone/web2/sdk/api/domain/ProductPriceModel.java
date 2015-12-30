package com.voyageone.web2.sdk.api.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductPrice and SKUPrice Model
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductPriceModel {

    //product Id
    private Long productId;
    //product Code
    private String productCode;

    //msrp价格区间-开始
    private Double msrpStart;
    //msrp价格区间-结束
    private Double msrpEnd;
    //建议市场价格区间-结束
    private Double retailPriceStart;
    //建议市场价格区间-结束
    private Double retailPriceEnd;
    //市场价格区间-开始
    private Double salePriceStart;
    //市场价格区间-结束
    private Double salePriceEnd;
    //当前销售价格区间-开始
    private Double currentPriceStart;
    //当前销售价格区间-结束
    private Double currentPriceEnd;

    private List<ProductSkuPriceModel> skuPrices = new ArrayList<>();

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Double getMsrpStart() {
        return msrpStart;
    }

    public void setMsrpStart(Double msrpStart) {
        this.msrpStart = msrpStart;
    }

    public Double getMsrpEnd() {
        return msrpEnd;
    }

    public void setMsrpEnd(Double msrpEnd) {
        this.msrpEnd = msrpEnd;
    }

    public Double getRetailPriceStart() {
        return retailPriceStart;
    }

    public void setRetailPriceStart(Double retailPriceStart) {
        this.retailPriceStart = retailPriceStart;
    }

    public Double getRetailPriceEnd() {
        return retailPriceEnd;
    }

    public void setRetailPriceEnd(Double retailPriceEnd) {
        this.retailPriceEnd = retailPriceEnd;
    }

    public Double getSalePriceStart() {
        return salePriceStart;
    }

    public void setSalePriceStart(Double salePriceStart) {
        this.salePriceStart = salePriceStart;
    }

    public Double getSalePriceEnd() {
        return salePriceEnd;
    }

    public void setSalePriceEnd(Double salePriceEnd) {
        this.salePriceEnd = salePriceEnd;
    }

    public Double getCurrentPriceStart() {
        return currentPriceStart;
    }

    public void setCurrentPriceStart(Double currentPriceStart) {
        this.currentPriceStart = currentPriceStart;
    }

    public Double getCurrentPriceEnd() {
        return currentPriceEnd;
    }

    public void setCurrentPriceEnd(Double currentPriceEnd) {
        this.currentPriceEnd = currentPriceEnd;
    }

    public List<ProductSkuPriceModel> getSkuPrices() {
        return skuPrices;
    }

    public void setSkuPrices(List<ProductSkuPriceModel> skuPrices) {
        this.skuPrices = skuPrices;
    }

    public void addSkuPrice(ProductSkuPriceModel model) {
        skuPrices.add(model);
    }
}
