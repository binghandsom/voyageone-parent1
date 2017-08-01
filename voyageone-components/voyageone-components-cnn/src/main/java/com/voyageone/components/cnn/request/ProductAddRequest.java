package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductAddResponse;

/**
 * Created by morse on 2017/7/31.
 */
public class ProductAddRequest extends AbstractCnnRequest<ProductAddResponse> {

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_ADD;
    }

    private String channelId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
