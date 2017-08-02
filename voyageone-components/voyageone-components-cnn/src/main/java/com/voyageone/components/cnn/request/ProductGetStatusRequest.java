package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductGetStatusResponse;

/**
 * 获取商品上下架状态
 * Created by morse on 2017/7/31.
 */
public class ProductGetStatusRequest extends CnnUrlRequest<ProductGetStatusResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_GET_STATUS;
    }

    private String numIId;

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }
}
