package com.voyageone.service.bean.cms.product;

/**
 * Created by rex.wu on 2016/12/1.
 */
public class CombinedSkuInfoBean {
    private String order_channel_id;
    private String cart_id;
    private String sku;
    private String real_sku;
    private String real_sku_price;
    private String real_sku_name;
    private String num_iid;

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getReal_sku() {
        return real_sku;
    }

    public void setReal_sku(String real_sku) {
        this.real_sku = real_sku;
    }

    public String getReal_sku_price() {
        return real_sku_price;
    }

    public void setReal_sku_price(String real_sku_price) {
        this.real_sku_price = real_sku_price;
    }

    public String getReal_sku_name() {
        return real_sku_name;
    }

    public void setReal_sku_name(String real_sku_name) {
        this.real_sku_name = real_sku_name;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }
}
