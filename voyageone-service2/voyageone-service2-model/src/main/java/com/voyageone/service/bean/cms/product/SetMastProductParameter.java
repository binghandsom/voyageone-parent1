package com.voyageone.service.bean.cms.product;

/**
 * Created by dell on 2016/7/18.
 */
public class SetMastProductParameter {
    String channelId;
    int cartId;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    String productCode;
}
