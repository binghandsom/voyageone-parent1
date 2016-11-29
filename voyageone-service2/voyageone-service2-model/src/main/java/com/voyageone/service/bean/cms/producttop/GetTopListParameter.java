package com.voyageone.service.bean.cms.producttop;

/**
 * Created by dell on 2016/11/29.
 */
public class GetTopListParameter {
    int cartId;//平台id
    String pCatId;//分类id

    public String getpCatId() {
        return pCatId;
    }

    public void setpCatId(String pCatId) {
        this.pCatId = pCatId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
