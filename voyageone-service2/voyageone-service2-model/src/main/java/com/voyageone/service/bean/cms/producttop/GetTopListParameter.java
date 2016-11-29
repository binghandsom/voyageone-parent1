package com.voyageone.service.bean.cms.producttop;

/**
 * Created by dell on 2016/11/29.
 */
public class GetTopListParameter {
    String channelId;
    int cartId;//平台id
    String pCatId;

    public String getpCatId() {
        return pCatId;
    }

    public void setpCatId(String pCatId) {
        this.pCatId = pCatId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
