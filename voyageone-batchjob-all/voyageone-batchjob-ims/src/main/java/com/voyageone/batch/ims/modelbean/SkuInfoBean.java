package com.voyageone.batch.ims.modelbean;

/**
 * Created by Leo on 15-7-30.
 */
public class SkuInfoBean {
    private String order_channel_id;
    private String code;
    private String sku;
    private int qty_china;

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty_china() {
        return qty_china;
    }

    public void setQty_china(int qty_china) {
        this.qty_china = qty_china;
    }
}
