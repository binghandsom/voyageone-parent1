package com.voyageone.components.intltarget.bean.inventory;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetInventoryProduct {

    private int product_id;
    private int available_to_promise_quantity;
    private int estore_back_order_available_to_promise_quantity;
    private int preorder_available_to_promise_quantity;
    private int online_available_to_promise_quantity;
    private int stores_available_to_promise_quantity;
    private int network_demand;
    private int sum_location_available_to_promise_quantity;
    private int oms_back_order_sum;
    private int estore_back_order_demand_sum;
    private int back_order_quantity_limit;
    private int back_order_quantity_count;
    private int back_order_quantity_available;
    private int preorder_demand_sum;
    private int subscription_demand_sum;
    private int online_demand_sum;
    private int ebay_demand_sum;
    private int channel_holdback_target_estore;
    private int margin_protection;
    private String message;
    private String more_info;

    //~~~~~~~~~getter setter

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getAvailable_to_promise_quantity() {
        return available_to_promise_quantity;
    }

    public void setAvailable_to_promise_quantity(int available_to_promise_quantity) {
        this.available_to_promise_quantity = available_to_promise_quantity;
    }

    public int getEstore_back_order_available_to_promise_quantity() {
        return estore_back_order_available_to_promise_quantity;
    }

    public void setEstore_back_order_available_to_promise_quantity(int estore_back_order_available_to_promise_quantity) {
        this.estore_back_order_available_to_promise_quantity = estore_back_order_available_to_promise_quantity;
    }

    public int getPreorder_available_to_promise_quantity() {
        return preorder_available_to_promise_quantity;
    }

    public void setPreorder_available_to_promise_quantity(int preorder_available_to_promise_quantity) {
        this.preorder_available_to_promise_quantity = preorder_available_to_promise_quantity;
    }

    public int getOnline_available_to_promise_quantity() {
        return online_available_to_promise_quantity;
    }

    public void setOnline_available_to_promise_quantity(int online_available_to_promise_quantity) {
        this.online_available_to_promise_quantity = online_available_to_promise_quantity;
    }

    public int getStores_available_to_promise_quantity() {
        return stores_available_to_promise_quantity;
    }

    public void setStores_available_to_promise_quantity(int stores_available_to_promise_quantity) {
        this.stores_available_to_promise_quantity = stores_available_to_promise_quantity;
    }

    public int getNetwork_demand() {
        return network_demand;
    }

    public void setNetwork_demand(int network_demand) {
        this.network_demand = network_demand;
    }

    public int getSum_location_available_to_promise_quantity() {
        return sum_location_available_to_promise_quantity;
    }

    public void setSum_location_available_to_promise_quantity(int sum_location_available_to_promise_quantity) {
        this.sum_location_available_to_promise_quantity = sum_location_available_to_promise_quantity;
    }

    public int getOms_back_order_sum() {
        return oms_back_order_sum;
    }

    public void setOms_back_order_sum(int oms_back_order_sum) {
        this.oms_back_order_sum = oms_back_order_sum;
    }

    public int getEstore_back_order_demand_sum() {
        return estore_back_order_demand_sum;
    }

    public void setEstore_back_order_demand_sum(int estore_back_order_demand_sum) {
        this.estore_back_order_demand_sum = estore_back_order_demand_sum;
    }

    public int getBack_order_quantity_limit() {
        return back_order_quantity_limit;
    }

    public void setBack_order_quantity_limit(int back_order_quantity_limit) {
        this.back_order_quantity_limit = back_order_quantity_limit;
    }

    public int getBack_order_quantity_count() {
        return back_order_quantity_count;
    }

    public void setBack_order_quantity_count(int back_order_quantity_count) {
        this.back_order_quantity_count = back_order_quantity_count;
    }

    public int getBack_order_quantity_available() {
        return back_order_quantity_available;
    }

    public void setBack_order_quantity_available(int back_order_quantity_available) {
        this.back_order_quantity_available = back_order_quantity_available;
    }

    public int getPreorder_demand_sum() {
        return preorder_demand_sum;
    }

    public void setPreorder_demand_sum(int preorder_demand_sum) {
        this.preorder_demand_sum = preorder_demand_sum;
    }

    public int getSubscription_demand_sum() {
        return subscription_demand_sum;
    }

    public void setSubscription_demand_sum(int subscription_demand_sum) {
        this.subscription_demand_sum = subscription_demand_sum;
    }

    public int getOnline_demand_sum() {
        return online_demand_sum;
    }

    public void setOnline_demand_sum(int online_demand_sum) {
        this.online_demand_sum = online_demand_sum;
    }

    public int getEbay_demand_sum() {
        return ebay_demand_sum;
    }

    public void setEbay_demand_sum(int ebay_demand_sum) {
        this.ebay_demand_sum = ebay_demand_sum;
    }

    public int getChannel_holdback_target_estore() {
        return channel_holdback_target_estore;
    }

    public void setChannel_holdback_target_estore(int channel_holdback_target_estore) {
        this.channel_holdback_target_estore = channel_holdback_target_estore;
    }

    public int getMargin_protection() {
        return margin_protection;
    }

    public void setMargin_protection(int margin_protection) {
        this.margin_protection = margin_protection;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMore_info() {
        return more_info;
    }

    public void setMore_info(String more_info) {
        this.more_info = more_info;
    }
}
