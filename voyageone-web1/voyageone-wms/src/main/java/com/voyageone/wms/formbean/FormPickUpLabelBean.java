package com.voyageone.wms.formbean;

import com.voyageone.wms.modelbean.ReservationBean;

/**
 * 捡货单专用Bean
 * Created by Jack on 5/27/2015.
 *
 * @author Jack
 */
public class FormPickUpLabelBean  {

    // 订单号
    public String order_number;

    // 预计到货期
    public String expected_ship_date;

    // 分库
    public String store;

    // 收件名
    public String ship_name;

    // 收件公司
    public String ship_company;

    // 收件地址
    public String ship_address;

    // 收件地址2
    public String ship_address2;

    // 收件城市
    public String ship_city;

    // 收件省
    public String ship_state;

    // 收件邮编
    public String ship_zip;

    // 收件国家
    public String ship_country;

    // 发货方
    public String shipping;

    // 货品名称
    public String product;

    // SKU
    public String sku;

    // 配货号
    public String reservation_id;

    // 发货渠道
    public String ship_channel;

    // 数量
    public String qty;

    // 渠道编号
    public String cart_id;

    // 捡货单类别
    public String label_type;

    // 订单渠道名
    private String order_channel_name;

    // 物品状态名
    private String status_name;

    // 创建时间（本地时间）
    private String created_local;

    // 更新时间（本地时间）
    private String modified_local;

    private String skuList;

    public String getSkuList() {
        return skuList;
    }

    public void setSkuList(String skuList) {
        this.skuList = skuList;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getExpected_ship_date() {
        return expected_ship_date;
    }

    public void setExpected_ship_date(String expected_ship_date) {
        this.expected_ship_date = expected_ship_date;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getShip_company() {
        return ship_company;
    }

    public void setShip_company(String ship_company) {
        this.ship_company = ship_company;
    }

    public String getShip_address() {
        return ship_address;
    }

    public void setShip_address(String ship_address) {
        this.ship_address = ship_address;
    }

    public String getShip_address2() {
        return ship_address2;
    }

    public void setShip_address2(String ship_address2) {
        this.ship_address2 = ship_address2;
    }

    public String getShip_city() {
        return ship_city;
    }

    public void setShip_city(String ship_city) {
        this.ship_city = ship_city;
    }

    public String getShip_state() {
        return ship_state;
    }

    public void setShip_state(String ship_state) {
        this.ship_state = ship_state;
    }

    public String getShip_zip() {
        return ship_zip;
    }

    public void setShip_zip(String ship_zip) {
        this.ship_zip = ship_zip;
    }

    public String getShip_country() {
        return ship_country;
    }

    public void setShip_country(String ship_country) {
        this.ship_country = ship_country;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getShip_channel() {
        return ship_channel;
    }

    public void setShip_channel(String ship_channel) {
        this.ship_channel = ship_channel;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getLabel_type() {
        return label_type;
    }

    public void setLabel_type(String label_type) {
        this.label_type = label_type;
    }

    public String getOrder_channel_name() {
        return order_channel_name;
    }

    public void setOrder_channel_name(String order_channel_name) {
        this.order_channel_name = order_channel_name;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getCreated_local() {
        return created_local;
    }

    public void setCreated_local(String created_local) {
        this.created_local = created_local;
    }

    public String getModified_local() {
        return modified_local;
    }

    public void setModified_local(String modified_local) {
        this.modified_local = modified_local;
    }
}
