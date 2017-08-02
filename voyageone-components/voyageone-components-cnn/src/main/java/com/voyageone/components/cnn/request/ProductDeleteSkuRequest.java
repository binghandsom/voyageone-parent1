package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductDeleteSkuResponse;

/**
 * 删除sku,逻辑删除
 * Created by morse on 2017/7/31.
 */
public class ProductDeleteSkuRequest extends CnnUrlRequest<ProductDeleteSkuResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_DELETE_SKU;
    }

    private String numIId;
    private String skuCode;

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
}
