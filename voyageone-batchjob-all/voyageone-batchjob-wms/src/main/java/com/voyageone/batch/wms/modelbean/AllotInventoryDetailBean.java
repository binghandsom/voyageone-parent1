package com.voyageone.batch.wms.modelbean;

/**
 * @author jack
 * 分配库存Bean
 */
public class AllotInventoryDetailBean {

    /**
     * 订单号
     */
    private String order_number;

    /**
     * 物品Item
     */
    private String item_number;

    /**
     * 网络订单号
     */
    private String source_order_id;

    /**
     * 订单渠道
     */
    private String order_channel_id;

    /**
     * 产品
     */
    private String product;

    /**
     * 平台渠道
     */
    private String cart_id;

    /**
     * SKU
     */
    private String sku;

    /**
     * 分配库存标志位
     */
    private String res_allot_flg;

    /**
     * 物品ID
     */
    private String res_id;

    /**
     * SKU错误标志位
     */
    private String sku_error_flg;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    public String getRes_allot_flg() {
        return res_allot_flg;
    }

    public void setRes_allot_flg(String res_allot_flg) {
        this.res_allot_flg = res_allot_flg;
    }

    public String getRes_id() {
        return res_id;
    }

    public void setRes_id(String res_id) {
        this.res_id = res_id;
    }

    public String getSku_error_flg() {
        return sku_error_flg;
    }

    public void setSku_error_flg(String sku_error_flg) {
        this.sku_error_flg = sku_error_flg;
    }
}
