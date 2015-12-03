package com.voyageone.wms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;
import com.voyageone.wms.modelbean.ReservationBean;

import java.util.List;
import java.util.Map;

/**
 * tt_reservation 外加 barcode以及画面显示用项目
 * Created by Jack on 5/21/2015.
 *
 * @author Jack
 */
public class FormPickupBean  extends ReservationBean {

    // 订单渠道名
    private String order_channel_name;

    // 物品状态名
    private String status_name;

    // barcode
    private String barcode;

    // 创建时间（本地时间）
    private String created_local;

    // 更新时间（本地时间）
    private String modified_local;

    // 拣货单类型
    private String label_type;

    // 数量
    public String qty;

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

    // 港口
    public String port;

    // 仓库种别
    public String store_kind;

    // 货架名称
    public String  location_name;

    // 仓库名称
    public String  store_name;

    // 仓库所属渠道
    public String  store_order_channel_id;

    // 品牌方SKU
    public String  client_sku;

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getLabel_type() {
        return label_type;
    }

    public void setLabel_type(String label_type) {
        this.label_type = label_type;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getStore_kind() {
        return store_kind;
    }

    public void setStore_kind(String store_kind) {
        this.store_kind = store_kind;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_order_channel_id() {
        return store_order_channel_id;
    }

    public void setStore_order_channel_id(String store_order_channel_id) {
        this.store_order_channel_id = store_order_channel_id;
    }

    public String getClient_sku() {
        return client_sku;
    }

    public void setClient_sku(String client_sku) {
        this.client_sku = client_sku;
    }
}
