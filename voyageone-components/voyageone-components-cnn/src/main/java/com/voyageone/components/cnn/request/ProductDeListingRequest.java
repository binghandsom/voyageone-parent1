package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductDeListingResponse;

/**
 * 商品下架
 * Created by morse on 2017/7/31.
 */
public class ProductDeListingRequest extends CnnUrlRequest<ProductDeListingResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_DELISTING;
    }

    private String numIId;

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }
}
