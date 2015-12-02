package com.voyageone.bi.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Kylin on 2015/7/21.
 */
public class TranspotationBean {
    private int seq;
    private String invoice_num;
    private int port_id;
    private Date process_time;
    private String tracking_no;
    private String ship_city;
    private String goods_type;
    private BigDecimal goods_weight_g;
    private BigDecimal transpotation_amount;
    private String tracking_type;
    private int goods_qty;
    private String customer_level;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal volumn_weight_g;
    private String organization_num;
    private String main_waybill_num;
    private String notes;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getInvoice_num() {
        return invoice_num;
    }

    public void setInvoice_num(String invoice_num) {
        this.invoice_num = invoice_num;
    }

    public int getPort_id() {
        return port_id;
    }

    public void setPort_id(int port_id) {
        this.port_id = port_id;
    }

    public Date getProcess_time() {
        return process_time;
    }

    public void setProcess_time(Date process_time) {
        this.process_time = process_time;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public String getShip_city() {
        return ship_city;
    }

    public void setShip_city(String ship_city) {
        this.ship_city = ship_city;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public BigDecimal getGoods_weight_g() {
        return goods_weight_g;
    }

    public void setGoods_weight_g(BigDecimal goods_weight_g) {
        this.goods_weight_g = goods_weight_g;
    }

    public BigDecimal getTranspotation_amount() {
        return transpotation_amount;
    }

    public void setTranspotation_amount(BigDecimal transpotation_amount) {
        this.transpotation_amount = transpotation_amount;
    }

    public String getTracking_type() {
        return tracking_type;
    }

    public void setTracking_type(String tracking_type) {
        this.tracking_type = tracking_type;
    }

    public int getGoods_qty() {
        return goods_qty;
    }

    public void setGoods_qty(int goods_qty) {
        this.goods_qty = goods_qty;
    }

    public String getCustomer_level() {
        return customer_level;
    }

    public void setCustomer_level(String customer_level) {
        this.customer_level = customer_level;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getVolumn_weight_g() {
        return volumn_weight_g;
    }

    public void setVolumn_weight_g(BigDecimal volumn_weight_g) {
        this.volumn_weight_g = volumn_weight_g;
    }

    public String getOrganization_num() {
        return organization_num;
    }

    public void setOrganization_num(String organization_num) {
        this.organization_num = organization_num;
    }

    public String getMain_waybill_num() {
        return main_waybill_num;
    }

    public void setMain_waybill_num(String main_waybill_num) {
        this.main_waybill_num = main_waybill_num;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
