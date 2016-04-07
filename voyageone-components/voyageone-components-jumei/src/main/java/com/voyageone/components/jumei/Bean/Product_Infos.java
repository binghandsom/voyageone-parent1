package com.voyageone.components.jumei.Bean;

import java.util.List;

/**
 * Created by sn3 on 2015-07-18.
 */
public class Product_Infos extends JmBaseBean {

    private String deal_hash_id;
    private String deal_price;
    private String quantity;
    private String sku_no;
    private String settlement_price;
    private String deal_short_name;
    private String supplier_code;
    private String upc_code;
    private String customs_product_number;
    private String attribute;
    private String is_bom;
    private List<Virtual_Data> virtual_data;

    public String getDeal_hash_id() {
        return deal_hash_id;
    }

    public void setDeal_hash_id(String deal_hash_id) {
        this.deal_hash_id = deal_hash_id;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSku_no() {
        return sku_no;
    }

    public void setSku_no(String sku_no) {
        this.sku_no = sku_no;
    }

    public String getSettlement_price() {
        return settlement_price;
    }

    public void setSettlement_price(String settlement_price) {
        this.settlement_price = settlement_price;
    }

    public String getDeal_short_name() {
        return deal_short_name;
    }

    public void setDeal_short_name(String deal_short_name) {
        this.deal_short_name = deal_short_name;
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

    public String getIs_bom() {
        return is_bom;
    }

    public void setIs_bom(String is_bom) {
        this.is_bom = is_bom;
    }

    public List<Virtual_Data> getVirtual_data() {
        return virtual_data;
    }

    public void setVirtual_data(List<Virtual_Data> virtual_data) {
        this.virtual_data = virtual_data;
    }
}
