package com.voyageone.web2.cms.bean;

/**
 * Created by jonasvlag on 16/7/5.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class PriceLogBean {

    private String sku;
    private String code;
    private String cart;
    private int offset;
    private int limit;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
