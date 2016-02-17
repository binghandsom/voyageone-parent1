package com.voyageone.bi.disbean;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Kylin on 2015/7/23.
 */
public class TaxDetailReportDisBean {

    private int year_calc;
    private int month_calc;
    private String order_channel_name;
    private String tracking_no;
    private String syn_ship_no;
    private String main_waybill_num;
    private String source_order_id;
    private String client_order_number;
    private String pay_in_warrant_num;
    private BigDecimal actual_shipped_weightLB;
    private BigDecimal shipped_weightLB;
    private BigDecimal goods_weight_lb;
    private BigDecimal express_weight_lb;
    private String ship_date;
    private BigInteger order_number;
    private BigDecimal tax_actual;
    private BigDecimal tax_actual_usd;
    private BigDecimal exchange_rate;

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

    public String getSyn_ship_no() {
        return syn_ship_no;
    }

    public void setSyn_ship_no(String syn_ship_no) {
        this.syn_ship_no = syn_ship_no;
    }

    public String getMain_waybill_num() {
        return main_waybill_num;
    }

    public void setMain_waybill_num(String main_waybill_num) {
        this.main_waybill_num = main_waybill_num;
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

    public String getPay_in_warrant_num() {
        return pay_in_warrant_num;
    }

    public void setPay_in_warrant_num(String pay_in_warrant_num) {
        this.pay_in_warrant_num = pay_in_warrant_num;
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

    public BigInteger getOrder_number() {
        return order_number;
    }

    public void setOrder_number(BigInteger order_number) {
        this.order_number = order_number;
    }

    public BigDecimal getTax_actual() {
        return tax_actual;
    }

    public void setTax_actual(BigDecimal tax_actual) {
        this.tax_actual = tax_actual;
    }

    public BigDecimal getTax_actual_usd() {
        return tax_actual_usd;
    }

    public void setTax_actual_usd(BigDecimal tax_actual_usd) {
        this.tax_actual_usd = tax_actual_usd;
    }

    public BigDecimal getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(BigDecimal exchange_rate) {
        this.exchange_rate = exchange_rate;
    }
}
