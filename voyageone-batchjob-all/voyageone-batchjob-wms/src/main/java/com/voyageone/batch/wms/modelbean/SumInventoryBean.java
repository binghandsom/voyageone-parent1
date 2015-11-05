package com.voyageone.batch.wms.modelbean;

/**
 * 以SKU集计物理库存
 *
 * Created by Fred on 15/11/04.
 */
public class SumInventoryBean {

    private String order_channel_id;
    private String store_id;
    private String sku;
    private String barcode;
    private String qty;
    private String res_qty;
    private String sales_price;
    private String msrp;
    private String brand;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRes_qty() {
        return res_qty;
    }

    public void setRes_qty(String res_qty) {
        this.res_qty = res_qty;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getSales_price() {
        return sales_price;
    }

    public void setSales_price(String sales_price) {
        this.sales_price = sales_price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
