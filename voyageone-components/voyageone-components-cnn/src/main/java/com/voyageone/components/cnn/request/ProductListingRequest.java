package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductListingResponse;

/**
 * 商品上架
 * Created by morse on 2017/7/31.
 */
public class ProductListingRequest extends CnnUrlRequest<ProductListingResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_LISTING;
    }

    private String numIId;

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }
}
