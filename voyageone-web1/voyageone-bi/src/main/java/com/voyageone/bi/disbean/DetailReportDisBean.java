package com.voyageone.bi.disbean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Kylin on 2015/7/23.
 */
public class DetailReportDisBean {

    private int year_calc;
    private int month_calc;
    private String order_channel_name;
    private String tracking_no;
    private String source_order_id;
    private String client_order_number;
    private BigInteger order_number;
    private BigDecimal actual_shipped_weightLB;
    private BigDecimal shipped_weightLB;
    private BigDecimal goods_weight_lb;
    private BigDecimal express_weight_lb;
    private String ship_date;
    private BigDecimal tax_actual;
    private BigDecimal transpotation_amount;
    private BigDecimal mail_fee;
    private BigDecimal ground_handling_fee;
    private BigDecimal storage_charges;

    public int getYear_calc() {
        return year_calc;
    }

    public void setYear_calc(int year_calc) {
        this.year_calc = year_calc;
    }

    public int getMonth_calc() {
        return month_calc;
    }

    public void setMonth_calc(int month_calc) {
        this.month_calc = month_calc;
    }

    public String getOrder_channel_name() {
        return order_channel_name;
    }

    public void setOrder_channel_name(String order_channel_name) {
        this.order_channel_name = order_channel_name;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
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

    public BigInteger getOrder_number() {
        return order_number;
    }

    public void setOrder_number(BigInteger order_number) {
        this.order_number = order_number;
    }

    public BigDecimal getActual_shipped_weightLB() {
        return actual_shipped_weightLB;
    }

    public void setActual_shipped_weightLB(BigDecimal actual_shipped_weightLB) {
        this.actual_shipped_weightLB = actual_shipped_weightLB;
    }

    public BigDecimal getShipped_weightLB() {
        return shipped_weightLB;
    }

    public void setShipped_weightLB(BigDecimal shipped_weightLB) {
        this.shipped_weightLB = shipped_weightLB;
    }

    public BigDecimal getGoods_weight_lb() {
        return goods_weight_lb;
    }

    public void setGoods_weight_lb(BigDecimal goods_weight_lb) {
        this.goods_weight_lb = goods_weight_lb;
    }

    public BigDecimal getExpress_weight_lb() {
        return express_weight_lb;
    }

    public void setExpress_weight_lb(BigDecimal express_weight_lb) {
        this.express_weight_lb = express_weight_lb;
    }

    public String getShip_date() {
        return ship_date;
    }

    public void setShip_date(String ship_date) {
        this.ship_date = ship_date;
    }

    public BigDecimal getTax_actual() {
        return tax_actual;
    }

    public void setTax_actual(BigDecimal tax_actual) {
        this.tax_actual = tax_actual;
    }

    public BigDecimal getTranspotation_amount() {
        return transpotation_amount;
    }

    public void setTranspotation_amount(BigDecimal transpotation_amount) {
        this.transpotation_amount = transpotation_amount;
    }

    public BigDecimal getMail_fee() {
        return mail_fee;
    }

    public void setMail_fee(BigDecimal mail_fee) {
        this.mail_fee = mail_fee;
    }

    public BigDecimal getGround_handling_fee() {
        return ground_handling_fee;
    }

    public void setGround_handling_fee(BigDecimal ground_handling_fee) {
        this.ground_handling_fee = ground_handling_fee;
    }

    public BigDecimal getStorage_charges() {
        return storage_charges;
    }

    public void setStorage_charges(BigDecimal storage_charges) {
        this.storage_charges = storage_charges;
    }
}
