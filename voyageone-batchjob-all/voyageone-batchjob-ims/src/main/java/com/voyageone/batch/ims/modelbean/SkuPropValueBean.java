package com.voyageone.batch.ims.modelbean;

/**
 * Created by Leo on 15-9-17.
 */
public class SkuPropValueBean {
    private String sku;
    private String prop_name;
    private String prop_value;

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
}
