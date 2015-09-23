package com.voyageone.batch.wms.modelbean;

/**
 * Created by fred on 2015/6/29.
 */
public class ClientInventoryBean {
    private String order_channel_id;
    private String product_id;
    private String color_id;
    private String size_id;
    private String qty;
    private String store_id;
    private String ItemCode;
    private String sku;
    private String size;
    private String barcode;
    private String syn_flg;
    private String sim_flg;
    private String active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;
    private String client_sku;

    public String getClient_sku() {
        return client_sku;
    }

    public void setClient_sku(String client_sku) {
        this.client_sku = client_sku;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSyn_flg() {
        return syn_flg;
    }

    public void setSyn_flg(String syn_flg) {
        this.syn_flg = syn_flg;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSim_flg() {
        return sim_flg;
    }

    public void setSim_flg(String sim_flg) {
        this.sim_flg = sim_flg;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
