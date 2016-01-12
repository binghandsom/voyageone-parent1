package com.voyageone.bi.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Kylin on 2015/7/21.
 */
public class OfficialTaxBean {
    private String year_calc;
    private String month_calc;
    private String date_calc;
    private String ship_date;
    private String port_id;
    private String tax_object_id;
    private String channel_code;
    private String order_channel_name;
    private String shipment_id;
    private BigInteger order_number;
    private String source_order_id;
    private String client_order_number;
    private BigInteger reservation_id;
    private String syn_ship_no;
    private BigInteger item_number;
    private String tracking_no;
    private String main_waybill_num;
    private String sku;
    private String hs_code_use;
    private BigDecimal rate;
    private BigDecimal rmb_price;
    private BigDecimal exchange_rate;
    private BigDecimal declare_price;
    private int declare_quantity;
    private BigDecimal custom_duties;

    public String getYear_calc() {
        return year_calc;
    }

    public void setYear_calc(String year_calc) {
        this.year_calc = year_calc;
    }

    public String getMonth_calc() {
        return month_calc;
    }

    public void setMonth_calc(String month_calc) {
        this.month_calc = month_calc;
    }

    public String getDate_calc() {
        return date_calc;
    }

    public void setDate_calc(String date_calc) {
        this.date_calc = date_calc;
    }

    public String getShip_date() {
        return ship_date;
    }

    public void setShip_date(String ship_date) {
        this.ship_date = ship_date;
    }

    public String getPort_id() {
        return port_id;
    }

    public void setPort_id(String port_id) {
        this.port_id = port_id;
    }

    public String getTax_object_id() {
        return tax_object_id;
    }

    public void setTax_object_id(String tax_object_id) {
        this.tax_object_id = tax_object_id;
    }

    public String getChannel_code() {
        return channel_code;
    }

    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }

    public String getOrder_channel_name() {
        return order_channel_name;
    }

    public void setOrder_channel_name(String order_channel_name) {
        this.order_channel_name = order_channel_name;
    }

    public String getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(String shipment_id) {
        this.shipment_id = shipment_id;
    }

    public BigInteger getOrder_number() {
        return order_number;
    }

    public void setOrder_number(BigInteger order_number) {
        this.order_number = order_number;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getClient_order_number() {
        return client_order_number;
    }

    public void setClient_order_number(String client_order_number) {
        this.client_order_number = client_order_number;
    }

    public BigInteger getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(BigInteger reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getSyn_ship_no() {
        return syn_ship_no;
    }

    public void setSyn_ship_no(String syn_ship_no) {
        this.syn_ship_no = syn_ship_no;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public String getMain_waybill_num() {
        return main_waybill_num;
    }

    public void setMain_waybill_num(String main_waybill_num) {
        this.main_waybill_num = main_waybill_num;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getHs_code_use() {
        return hs_code_use;
    }

    public void setHs_code_use(String hs_code_use) {
        this.hs_code_use = hs_code_use;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getDeclare_price() {
        return declare_price;
    }

    public void setDeclare_price(BigDecimal declare_price) {
        this.declare_price = declare_price;
    }

    public int getDeclare_quantity() {
        return declare_quantity;
    }

    public void setDeclare_quantity(int declare_quantity) {
        this.declare_quantity = declare_quantity;
    }

    public BigDecimal getCustom_duties() {
        return custom_duties;
    }

    public void setCustom_duties(BigDecimal custom_duties) {
        this.custom_duties = custom_duties;
    }

    public BigInteger getItem_number() {
        return item_number;
    }

    public void setItem_number(BigInteger item_number) {
        this.item_number = item_number;
    }

    public BigDecimal getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(BigDecimal exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public BigDecimal getRmb_price() {
        return rmb_price;
    }

    public void setRmb_price(BigDecimal rmb_price) {
        this.rmb_price = rmb_price;
    }
}
