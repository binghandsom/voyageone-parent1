package com.voyageone.web2.sdk.api.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductPrice and SKUPrice Model
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductPriceModel {

    //product Id
    private Long productId;
    //product Code
    private String productCode;

    private Integer priceChange;

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

    public Integer getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(Integer priceChange) {
        this.priceChange = priceChange;
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
