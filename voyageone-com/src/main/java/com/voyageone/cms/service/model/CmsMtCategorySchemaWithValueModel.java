package com.voyageone.cms.service.model;

/**
 * Created by lewis on 15-12-15.
 */
public class CmsMtCategorySchemaWithValueModel extends CmsMtCategorySchemaModel{
    private String channelId;
    private int productId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
