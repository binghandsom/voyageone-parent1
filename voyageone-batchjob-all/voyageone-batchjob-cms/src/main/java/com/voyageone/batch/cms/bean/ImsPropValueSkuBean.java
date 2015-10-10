package com.voyageone.batch.cms.bean;

public class ImsPropValueSkuBean {
    String sku;
    String prop_name;
    String prop_value;
    String order_channel_id;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProp_name() {
        return prop_name;
    }

    public void setProp_name(String prop_name) {
        this.prop_name = prop_name;
    }

    public String getProp_value() {
        return prop_value;
    }

    public void setProp_value(String prop_value) {
        this.prop_value = prop_value;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }
}
