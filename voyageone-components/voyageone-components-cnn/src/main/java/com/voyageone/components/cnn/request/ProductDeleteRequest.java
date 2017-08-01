package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductDeleteResponse;

/**
 * Created by morse on 2017/7/31.
 */
public class ProductDeleteRequest extends CnnUrlRequest<ProductDeleteResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_DELETE;
    }

    private String numIId;

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }
}
