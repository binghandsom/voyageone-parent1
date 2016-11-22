package com.voyageone.service.bean.cms.CmsProductPlatformDetail;

/**
 * Created by dell on 2016/11/22.
 */
public class SaveCartSkuPriceParameter {
    double priceMsrp;//中国建议售价
    double priceSale;//中国最终售价
    long prodId;//
    int cartId;//



    public double getPriceMsrp() {
        return priceMsrp;
    }
    public void setPriceMsrp(double priceMsrp) {
        this.priceMsrp = priceMsrp;
    }
    public double getPriceSale() {
        return priceSale;
    }
    public void setPriceSale(double priceSale) {
        this.priceSale = priceSale;
    }
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
}
