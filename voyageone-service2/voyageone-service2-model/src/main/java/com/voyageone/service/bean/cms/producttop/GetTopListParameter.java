package com.voyageone.service.bean.cms.producttop;

/**
 * Created by dell on 2016/11/29.
 */
public class GetTopListParameter {
    int cartId;//平台id
    String sellerCatId;//店铺内分类

    public String getSellerCatId() {
        return sellerCatId;
    }

    public void setSellerCatId(String sellerCatId) {
        this.sellerCatId = sellerCatId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
