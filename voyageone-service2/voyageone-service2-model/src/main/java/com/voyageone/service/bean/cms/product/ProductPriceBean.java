package com.voyageone.service.bean.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductPrice and SKUPrice Model
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductPriceBean {

    //product Id
    private Long productId;
    //product Code
    private String productCode;

    private Integer priceChange;

    private List<ProductSkuPriceBean> skuPrices = new ArrayList<>();

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

    public List<ProductSkuPriceBean> getSkuPrices() {
        return skuPrices;
    }

    public void setSkuPrices(List<ProductSkuPriceBean> skuPrices) {
        this.skuPrices = skuPrices;
    }

    public void addSkuPrice(ProductSkuPriceBean model) {
        skuPrices.add(model);
    }


}
