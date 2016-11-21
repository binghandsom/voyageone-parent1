package com.voyageone.service.bean.cms.CmsProductPlatformDetail;

/**
 * Created by dell on 2016/11/21.
 */
public class SetCartSkuIsSaleParameter {
    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public boolean isSale() {
        return isSale;
    }

    public void setSale(boolean sale) {
        isSale = sale;
    }

    long prodId;
    int cartId;
    boolean isSale;
}
