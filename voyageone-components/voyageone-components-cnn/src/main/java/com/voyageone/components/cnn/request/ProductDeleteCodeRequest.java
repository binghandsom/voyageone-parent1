package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductDeleteCodeResponse;

/**
 * Created by morse on 2017/7/31.
 */
public class ProductDeleteCodeRequest extends CnnUrlRequest<ProductDeleteCodeResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_DELETE_CODE;
    }

    private String numIId;
    private String prodCode;

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
}
