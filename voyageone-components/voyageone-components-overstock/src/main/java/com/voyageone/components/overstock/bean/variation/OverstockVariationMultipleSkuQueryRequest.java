package com.voyageone.components.overstock.bean.variation;

import com.voyageone.components.overstock.bean.OverstockMultipleRequest;

/**
 * @author aooer 2016/6/7.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OverstockVariationMultipleSkuQueryRequest extends OverstockMultipleRequest {

    private String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
