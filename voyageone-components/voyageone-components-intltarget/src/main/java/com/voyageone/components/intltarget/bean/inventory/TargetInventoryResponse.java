package com.voyageone.components.intltarget.bean.inventory;

import java.util.List;

public class TargetInventoryResponse {

    public List<Products> products;

    /**
     * Products is the inner class of TargetInventoryResponse
     */
    public static class Products {
        public Double margin_protection;
        public List<String> multichannel_options;
        public Double back_order_quantity_available;
        public String availability;
        public Double stores_available_to_promise_quantity;
        public Double online_demand_sum;
        public Double sum_location_available_to_promise_quantity;
        public Double safety_stock;
        public Double channel_holdback_ebay;
        public Double limited_quantity_higher;
        public String product_id;
        public Double pre_order_available_to_promise_quantity;
        public Double back_order_quantity_limit;
        public Double limited_quantity_lower;
        public String id_type;
        public Boolean is_limited_quantity;
        public Boolean limited_quantity_enabled;
        public Integer online_protected_quantity;
        public Double oms_back_order_sum;
        public Double ebay_demand_sum;
        public Double available_to_promise_quantity;
        public Double subscription_demand_sum;
        public String street_date;
        public Double network_demand;
        public Double back_order_quantity_count;
        public String availability_status;
        public Double pre_order_demand_sum;
        public String release_date;
        public Double channel_holdback_target_estore;
        public String channel_id;
        public Double online_available_to_promise_quantity;
    }
}
