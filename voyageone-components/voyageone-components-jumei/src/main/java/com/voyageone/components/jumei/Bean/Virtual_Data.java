package com.voyageone.components.jumei.Bean;

/**
 * Created by sn3 on 2015-07-18.
 */
public class Virtual_Data extends JmBaseBean {

    private String sku_no;
    private String supplier_code;
    private String upc_code;
    private String customs_product_number;
    private String attribute;
    private String num;
    private String price;

    public String getSku_no() {
        return sku_no;
    }

    public void setSku_no(String sku_no) {
        this.sku_no = sku_no;
    }

    public String getSupplier_code() {
        return supplier_code;
    }

    public void setSupplier_code(String supplier_code) {
        this.supplier_code = supplier_code;
    }

    public String getUpc_code() {
        return upc_code;
    }

    public void setUpc_code(String upc_code) {
        this.upc_code = upc_code;
    }

    public String getCustoms_product_number() {
        return customs_product_number;
    }

    public void setCustoms_product_number(String customs_product_number) {
        this.customs_product_number = customs_product_number;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
