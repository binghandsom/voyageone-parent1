package com.voyageone.components.intltarget.bean.inventory;

import java.util.List;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetInventoryResponse {
    private List<Products> products;

    public List<Products> getProducts() {
        return this.products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }


    /**
     * Products is the inner class of TargetInventoryResponse
     */
    public static class Products {
        private Double margin_protection;

        public Double getMargin_protection() {
            return this.margin_protection;
        }

        public void setMargin_protection(Double margin_protection) {
            this.margin_protection = margin_protection;
        }

        private List<String> multichannel_options;

        public List<String> getMultichannel_options() {
            return this.multichannel_options;
        }

        public void setMultichannel_options(List<String> multichannel_options) {
            this.multichannel_options = multichannel_options;
        }

        private Double back_order_quantity_available;

        public Double getBack_order_quantity_available() {
            return this.back_order_quantity_available;
        }

        public void setBack_order_quantity_available(Double back_order_quantity_available) {
            this.back_order_quantity_available = back_order_quantity_available;
        }

        private String availability;

        public String getAvailability() {
            return this.availability;
        }

        public void setAvailability(String availability) {
            this.availability = availability;
        }

        private Double stores_available_to_promise_quantity;

        public Double getStores_available_to_promise_quantity() {
            return this.stores_available_to_promise_quantity;
        }

        public void setStores_available_to_promise_quantity(Double stores_available_to_promise_quantity) {
            this.stores_available_to_promise_quantity = stores_available_to_promise_quantity;
        }

        private Double online_demand_sum;

        public Double getOnline_demand_sum() {
            return this.online_demand_sum;
        }

        public void setOnline_demand_sum(Double online_demand_sum) {
            this.online_demand_sum = online_demand_sum;
        }

        private Double sum_location_available_to_promise_quantity;

        public Double getSum_location_available_to_promise_quantity() {
            return this.sum_location_available_to_promise_quantity;
        }

        public void setSum_location_available_to_promise_quantity(Double sum_location_available_to_promise_quantity) {
            this.sum_location_available_to_promise_quantity = sum_location_available_to_promise_quantity;
        }

        private Double safety_stock;

        public Double getSafety_stock() {
            return this.safety_stock;
        }

        public void setSafety_stock(Double safety_stock) {
            this.safety_stock = safety_stock;
        }

        private Double channel_holdback_ebay;

        public Double getChannel_holdback_ebay() {
            return this.channel_holdback_ebay;
        }

        public void setChannel_holdback_ebay(Double channel_holdback_ebay) {
            this.channel_holdback_ebay = channel_holdback_ebay;
        }

        private Double limited_quantity_higher;

        public Double getLimited_quantity_higher() {
            return this.limited_quantity_higher;
        }

        public void setLimited_quantity_higher(Double limited_quantity_higher) {
            this.limited_quantity_higher = limited_quantity_higher;
        }

        private String product_id;

        public String getProduct_id() {
            return this.product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        private Double pre_order_available_to_promise_quantity;

        public Double getPre_order_available_to_promise_quantity() {
            return this.pre_order_available_to_promise_quantity;
        }

        public void setPre_order_available_to_promise_quantity(Double pre_order_available_to_promise_quantity) {
            this.pre_order_available_to_promise_quantity = pre_order_available_to_promise_quantity;
        }

        private Double back_order_quantity_limit;

        public Double getBack_order_quantity_limit() {
            return this.back_order_quantity_limit;
        }

        public void setBack_order_quantity_limit(Double back_order_quantity_limit) {
            this.back_order_quantity_limit = back_order_quantity_limit;
        }

        private Double limited_quantity_lower;

        public Double getLimited_quantity_lower() {
            return this.limited_quantity_lower;
        }

        public void setLimited_quantity_lower(Double limited_quantity_lower) {
            this.limited_quantity_lower = limited_quantity_lower;
        }

        private String id_type;

        public String getId_type() {
            return this.id_type;
        }

        public void setId_type(String id_type) {
            this.id_type = id_type;
        }

        private Boolean is_limited_quantity;

        public Boolean getIs_limited_quantity() {
            return this.is_limited_quantity;
        }

        public void setIs_limited_quantity(Boolean is_limited_quantity) {
            this.is_limited_quantity = is_limited_quantity;
        }

        private Boolean limited_quantity_enabled;

        public Boolean getLimited_quantity_enabled() {
            return this.limited_quantity_enabled;
        }

        public void setLimited_quantity_enabled(Boolean limited_quantity_enabled) {
            this.limited_quantity_enabled = limited_quantity_enabled;
        }

        private Integer online_protected_quantity;

        public Integer getOnline_protected_quantity() {
            return this.online_protected_quantity;
        }

        public void setOnline_protected_quantity(Integer online_protected_quantity) {
            this.online_protected_quantity = online_protected_quantity;
        }

        private Double oms_back_order_sum;

        public Double getOms_back_order_sum() {
            return this.oms_back_order_sum;
        }

        public void setOms_back_order_sum(Double oms_back_order_sum) {
            this.oms_back_order_sum = oms_back_order_sum;
        }

        private Double ebay_demand_sum;

        public Double getEbay_demand_sum() {
            return this.ebay_demand_sum;
        }

        public void setEbay_demand_sum(Double ebay_demand_sum) {
            this.ebay_demand_sum = ebay_demand_sum;
        }

        private Double available_to_promise_quantity;

        public Double getAvailable_to_promise_quantity() {
            return this.available_to_promise_quantity;
        }

        public void setAvailable_to_promise_quantity(Double available_to_promise_quantity) {
            this.available_to_promise_quantity = available_to_promise_quantity;
        }

        private Double subscription_demand_sum;

        public Double getSubscription_demand_sum() {
            return this.subscription_demand_sum;
        }

        public void setSubscription_demand_sum(Double subscription_demand_sum) {
            this.subscription_demand_sum = subscription_demand_sum;
        }

        private String street_date;

        public String getStreet_date() {
            return this.street_date;
        }

        public void setStreet_date(String street_date) {
            this.street_date = street_date;
        }

        private Double network_demand;

        public Double getNetwork_demand() {
            return this.network_demand;
        }

        public void setNetwork_demand(Double network_demand) {
            this.network_demand = network_demand;
        }

        private Double back_order_quantity_count;

        public Double getBack_order_quantity_count() {
            return this.back_order_quantity_count;
        }

        public void setBack_order_quantity_count(Double back_order_quantity_count) {
            this.back_order_quantity_count = back_order_quantity_count;
        }

        private String availability_status;

        public String getAvailability_status() {
            return this.availability_status;
        }

        public void setAvailability_status(String availability_status) {
            this.availability_status = availability_status;
        }

        private Double pre_order_demand_sum;

        public Double getPre_order_demand_sum() {
            return this.pre_order_demand_sum;
        }

        public void setPre_order_demand_sum(Double pre_order_demand_sum) {
            this.pre_order_demand_sum = pre_order_demand_sum;
        }

        private String release_date;

        public String getRelease_date() {
            return this.release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        private Double channel_holdback_target_estore;

        public Double getChannel_holdback_target_estore() {
            return this.channel_holdback_target_estore;
        }

        public void setChannel_holdback_target_estore(Double channel_holdback_target_estore) {
            this.channel_holdback_target_estore = channel_holdback_target_estore;
        }

        private String channel_id;

        public String getChannel_id() {
            return this.channel_id;
        }

        public void setChannel_id(String channel_id) {
            this.channel_id = channel_id;
        }

        private Double online_available_to_promise_quantity;

        public Double getOnline_available_to_promise_quantity() {
            return this.online_available_to_promise_quantity;
        }

        public void setOnline_available_to_promise_quantity(Double online_available_to_promise_quantity) {
            this.online_available_to_promise_quantity = online_available_to_promise_quantity;
        }

    }

}
