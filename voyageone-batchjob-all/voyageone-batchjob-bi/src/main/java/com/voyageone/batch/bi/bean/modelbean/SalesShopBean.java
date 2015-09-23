package com.voyageone.batch.bi.bean.modelbean;

import java.math.BigDecimal;

/**
 * Created by Kylin on 2015/6/15.
 */
public class SalesShopBean {
    private int shop_id;
    private int process_date;
    private int pv_product_num;
    private int uv_store_home;
    private int pv_store_home;
    private int uv;
    private int uv_change_daily;
    private int uv_regular;
    private BigDecimal uv_regular_rate;
    private int pv;
    private int pv_change_daily;
    private int pv_product_num_pc;
    private int uv_store_home_pc;
    private int pv_store_home_pc;
    private BigDecimal visit_time_avg_store_home_pc;
    private int uv_pc;
    private int uv_regular_pc;
    private int pv_pc;
    private BigDecimal pv_avg_pc;
    private BigDecimal visit_time_avg_pc;
    private int uv_detail_pc;
    private int pv_detail_pc;
    private BigDecimal bounce_rate_pc;
    private BigDecimal pv_avg;
    private BigDecimal visit_time_avg;
    private int uv_detail;
    private int pv_detail;
    private BigDecimal bounce_rate;
    private int uv_new;
    private int uv_tb_app;
    private int pv_tb_app;
    private int uv_tm_app;
    private int pv_tm_app;
    private int uv_wap;
    private int pv_wap;
    private int pv_product_num_wireless;
    private int uv_store_home_wireless;
    private int pv_store_home_wireless;
    private int uv_wireless;
    private BigDecimal uv_wireless_rate;
    private int uv_regular_wireless;
    private int pv_wireless;
    private BigDecimal pv_avg_wireless;
    private BigDecimal visit_time_avg_wireless;
    private int uv_detail_wireless;
    private int pv_detail_wireless;
    private BigDecimal bounce_rate_wireless;
    private BigDecimal price_customer_unit;
    private int num_regular;
    private BigDecimal rate_num_regular;
    private BigDecimal price_customer_unit_pc;
    private BigDecimal amt_order_pc;
    private int num_order_customer_pc;
    private BigDecimal cvr_order_pc;
    private int num_f_order_paid_pc;
    private BigDecimal amt_paid_pc;
    private int num_regular_paid_pc;
    private int num_customer_paid_pc;
    private int qty_case_paid_pc;
    private int qty_product_paid_pc;
    private BigDecimal cvr_paid_pc;
    private int num_c_order_paid_pc;
    private BigDecimal cvr_paid_detail_pc;
    private int num_f_order;
    private BigDecimal amt_order;
    private int num_customer;
    private int num_f_order_paid;
    private BigDecimal amt_order_paid;
    private int num_customer_order_paid;
    private int num_case_order_paid;
    private int num_case_order;
    private BigDecimal cvr_order;
    private int num_c_order;
    private int num_new_customer;
    private BigDecimal avg_case_paid;
    private BigDecimal avg_num_c_order_paid;
    private int num_f_paid;
    private BigDecimal amt_paid;
    private BigDecimal amt_paid_change_daily;
    private int num_customer_paid;
    private int num_case_paid;
    private int num_product_paid;
    private BigDecimal avg_paid;
    private int num_c_order_paid;
    private BigDecimal amt_paid_tb_app;
    private int num_customer_paid_tb_app;
    private int num_case_paid_tb_app;
    private BigDecimal cvr_paid_tb_app;
    private BigDecimal amt_paid_tm_app;
    private int num_customer_paid_tm_app;
    private int num_case_paid_tm_app;
    private BigDecimal cvr_paid_tm_app;
    private BigDecimal amt_paid_wap;
    private int num_customer_paid_wap;
    private int num_case_paid_wap;
    private BigDecimal cvr_paid_wap;
    private BigDecimal price_customer_unit_wireless;
    private BigDecimal amt_order_wireless;
    private int num_customer_order_wireless;
    private BigDecimal cvr_order_wireless;
    private int num_f_order_paid_wireless;
    private BigDecimal amt_paid_wireless;
    private BigDecimal amt_rate_paid_wireless;
    private int num_regular_paid_wireless;
    private int num_customer_paid_wireless;
    private BigDecimal num_rate_customer_paid_wireless;
    private int num_case_paid_wireless;
    private int num_product_paid_wireless;
    private BigDecimal cvr_paid_wireless;
    private int num_c_order_paid_wireless;
    private int num_customer_low_dsr_com;
    private BigDecimal dsr_service;
    private BigDecimal dsr_describe;
    private BigDecimal amt_return;
    private int num_customer_return;
    private BigDecimal dsr_express;
    private int num_f_order_shipped;
    private int times_collect_store;
    private int times_collect_product;
    private int num_collect_customer_product;
    private int num_collect_customer_store;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getProcess_date() {
        return process_date;
    }

    public void setProcess_date(int process_date) {
        this.process_date = process_date;
    }

    public int getPv_product_num() {
        return pv_product_num;
    }

    public void setPv_product_num(int pv_product_num) {
        this.pv_product_num = pv_product_num;
    }

    public int getUv_store_home() {
        return uv_store_home;
    }

    public void setUv_store_home(int uv_store_home) {
        this.uv_store_home = uv_store_home;
    }

    public int getPv_store_home() {
        return pv_store_home;
    }

    public void setPv_store_home(int pv_store_home) {
        this.pv_store_home = pv_store_home;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getUv_change_daily() {
        return uv_change_daily;
    }

    public void setUv_change_daily(int uv_change_daily) {
        this.uv_change_daily = uv_change_daily;
    }

    public int getUv_regular() {
        return uv_regular;
    }

    public void setUv_regular(int uv_regular) {
        this.uv_regular = uv_regular;
    }

    public BigDecimal getUv_regular_rate() {
        return uv_regular_rate;
    }

    public void setUv_regular_rate(BigDecimal uv_regular_rate) {
        this.uv_regular_rate = uv_regular_rate;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getPv_change_daily() {
        return pv_change_daily;
    }

    public void setPv_change_daily(int pv_change_daily) {
        this.pv_change_daily = pv_change_daily;
    }

    public int getPv_product_num_pc() {
        return pv_product_num_pc;
    }

    public void setPv_product_num_pc(int pv_product_num_pc) {
        this.pv_product_num_pc = pv_product_num_pc;
    }

    public int getUv_store_home_pc() {
        return uv_store_home_pc;
    }

    public void setUv_store_home_pc(int uv_store_home_pc) {
        this.uv_store_home_pc = uv_store_home_pc;
    }

    public int getPv_store_home_pc() {
        return pv_store_home_pc;
    }

    public void setPv_store_home_pc(int pv_store_home_pc) {
        this.pv_store_home_pc = pv_store_home_pc;
    }

    public BigDecimal getVisit_time_avg_store_home_pc() {
        return visit_time_avg_store_home_pc;
    }

    public void setVisit_time_avg_store_home_pc(BigDecimal visit_time_avg_store_home_pc) {
        this.visit_time_avg_store_home_pc = visit_time_avg_store_home_pc;
    }

    public int getUv_pc() {
        return uv_pc;
    }

    public void setUv_pc(int uv_pc) {
        this.uv_pc = uv_pc;
    }

    public int getUv_regular_pc() {
        return uv_regular_pc;
    }

    public void setUv_regular_pc(int uv_regular_pc) {
        this.uv_regular_pc = uv_regular_pc;
    }

    public int getPv_pc() {
        return pv_pc;
    }

    public void setPv_pc(int pv_pc) {
        this.pv_pc = pv_pc;
    }

    public BigDecimal getPv_avg_pc() {
        return pv_avg_pc;
    }

    public void setPv_avg_pc(BigDecimal pv_avg_pc) {
        this.pv_avg_pc = pv_avg_pc;
    }

    public BigDecimal getVisit_time_avg_pc() {
        return visit_time_avg_pc;
    }

    public void setVisit_time_avg_pc(BigDecimal visit_time_avg_pc) {
        this.visit_time_avg_pc = visit_time_avg_pc;
    }

    public int getUv_detail_pc() {
        return uv_detail_pc;
    }

    public void setUv_detail_pc(int uv_detail_pc) {
        this.uv_detail_pc = uv_detail_pc;
    }

    public int getPv_detail_pc() {
        return pv_detail_pc;
    }

    public void setPv_detail_pc(int pv_detail_pc) {
        this.pv_detail_pc = pv_detail_pc;
    }

    public BigDecimal getBounce_rate_pc() {
        return bounce_rate_pc;
    }

    public void setBounce_rate_pc(BigDecimal bounce_rate_pc) {
        this.bounce_rate_pc = bounce_rate_pc;
    }

    public BigDecimal getPv_avg() {
        return pv_avg;
    }

    public void setPv_avg(BigDecimal pv_avg) {
        this.pv_avg = pv_avg;
    }

    public BigDecimal getVisit_time_avg() {
        return visit_time_avg;
    }

    public void setVisit_time_avg(BigDecimal visit_time_avg) {
        this.visit_time_avg = visit_time_avg;
    }

    public int getUv_detail() {
        return uv_detail;
    }

    public void setUv_detail(int uv_detail) {
        this.uv_detail = uv_detail;
    }

    public int getPv_detail() {
        return pv_detail;
    }

    public void setPv_detail(int pv_detail) {
        this.pv_detail = pv_detail;
    }

    public BigDecimal getBounce_rate() {
        return bounce_rate;
    }

    public void setBounce_rate(BigDecimal bounce_rate) {
        this.bounce_rate = bounce_rate;
    }

    public int getUv_new() {
        return uv_new;
    }

    public void setUv_new(int uv_new) {
        this.uv_new = uv_new;
    }

    public int getUv_tb_app() {
        return uv_tb_app;
    }

    public void setUv_tb_app(int uv_tb_app) {
        this.uv_tb_app = uv_tb_app;
    }

    public int getPv_tb_app() {
        return pv_tb_app;
    }

    public void setPv_tb_app(int pv_tb_app) {
        this.pv_tb_app = pv_tb_app;
    }

    public int getUv_tm_app() {
        return uv_tm_app;
    }

    public void setUv_tm_app(int uv_tm_app) {
        this.uv_tm_app = uv_tm_app;
    }

    public int getPv_tm_app() {
        return pv_tm_app;
    }

    public void setPv_tm_app(int pv_tm_app) {
        this.pv_tm_app = pv_tm_app;
    }

    public int getUv_wap() {
        return uv_wap;
    }

    public void setUv_wap(int uv_wap) {
        this.uv_wap = uv_wap;
    }

    public int getPv_wap() {
        return pv_wap;
    }

    public void setPv_wap(int pv_wap) {
        this.pv_wap = pv_wap;
    }

    public int getPv_product_num_wireless() {
        return pv_product_num_wireless;
    }

    public void setPv_product_num_wireless(int pv_product_num_wireless) {
        this.pv_product_num_wireless = pv_product_num_wireless;
    }

    public int getUv_store_home_wireless() {
        return uv_store_home_wireless;
    }

    public void setUv_store_home_wireless(int uv_store_home_wireless) {
        this.uv_store_home_wireless = uv_store_home_wireless;
    }

    public int getPv_store_home_wireless() {
        return pv_store_home_wireless;
    }

    public void setPv_store_home_wireless(int pv_store_home_wireless) {
        this.pv_store_home_wireless = pv_store_home_wireless;
    }

    public int getUv_wireless() {
        return uv_wireless;
    }

    public void setUv_wireless(int uv_wireless) {
        this.uv_wireless = uv_wireless;
    }

    public BigDecimal getUv_wireless_rate() {
        return uv_wireless_rate;
    }

    public void setUv_wireless_rate(BigDecimal uv_wireless_rate) {
        this.uv_wireless_rate = uv_wireless_rate;
    }

    public int getUv_regular_wireless() {
        return uv_regular_wireless;
    }

    public void setUv_regular_wireless(int uv_regular_wireless) {
        this.uv_regular_wireless = uv_regular_wireless;
    }

    public int getPv_wireless() {
        return pv_wireless;
    }

    public void setPv_wireless(int pv_wireless) {
        this.pv_wireless = pv_wireless;
    }

    public BigDecimal getPv_avg_wireless() {
        return pv_avg_wireless;
    }

    public void setPv_avg_wireless(BigDecimal pv_avg_wireless) {
        this.pv_avg_wireless = pv_avg_wireless;
    }

    public BigDecimal getVisit_time_avg_wireless() {
        return visit_time_avg_wireless;
    }

    public void setVisit_time_avg_wireless(BigDecimal visit_time_avg_wireless) {
        this.visit_time_avg_wireless = visit_time_avg_wireless;
    }

    public int getUv_detail_wireless() {
        return uv_detail_wireless;
    }

    public void setUv_detail_wireless(int uv_detail_wireless) {
        this.uv_detail_wireless = uv_detail_wireless;
    }

    public int getPv_detail_wireless() {
        return pv_detail_wireless;
    }

    public void setPv_detail_wireless(int pv_detail_wireless) {
        this.pv_detail_wireless = pv_detail_wireless;
    }

    public BigDecimal getBounce_rate_wireless() {
        return bounce_rate_wireless;
    }

    public void setBounce_rate_wireless(BigDecimal bounce_rate_wireless) {
        this.bounce_rate_wireless = bounce_rate_wireless;
    }

    public BigDecimal getPrice_customer_unit() {
        return price_customer_unit;
    }

    public void setPrice_customer_unit(BigDecimal price_customer_unit) {
        this.price_customer_unit = price_customer_unit;
    }

    public int getNum_regular() {
        return num_regular;
    }

    public void setNum_regular(int num_regular) {
        this.num_regular = num_regular;
    }

    public BigDecimal getRate_num_regular() {
        return rate_num_regular;
    }

    public void setRate_num_regular(BigDecimal rate_num_regular) {
        this.rate_num_regular = rate_num_regular;
    }

    public BigDecimal getPrice_customer_unit_pc() {
        return price_customer_unit_pc;
    }

    public void setPrice_customer_unit_pc(BigDecimal price_customer_unit_pc) {
        this.price_customer_unit_pc = price_customer_unit_pc;
    }

    public BigDecimal getAmt_order_pc() {
        return amt_order_pc;
    }

    public void setAmt_order_pc(BigDecimal amt_order_pc) {
        this.amt_order_pc = amt_order_pc;
    }

    public int getNum_order_customer_pc() {
        return num_order_customer_pc;
    }

    public void setNum_order_customer_pc(int num_order_customer_pc) {
        this.num_order_customer_pc = num_order_customer_pc;
    }

    public BigDecimal getCvr_order_pc() {
        return cvr_order_pc;
    }

    public void setCvr_order_pc(BigDecimal cvr_order_pc) {
        this.cvr_order_pc = cvr_order_pc;
    }

    public int getNum_f_order_paid_pc() {
        return num_f_order_paid_pc;
    }

    public void setNum_f_order_paid_pc(int num_f_order_paid_pc) {
        this.num_f_order_paid_pc = num_f_order_paid_pc;
    }

    public BigDecimal getAmt_paid_pc() {
        return amt_paid_pc;
    }

    public void setAmt_paid_pc(BigDecimal amt_paid_pc) {
        this.amt_paid_pc = amt_paid_pc;
    }

    public int getNum_regular_paid_pc() {
        return num_regular_paid_pc;
    }

    public void setNum_regular_paid_pc(int num_regular_paid_pc) {
        this.num_regular_paid_pc = num_regular_paid_pc;
    }

    public int getNum_customer_paid_pc() {
        return num_customer_paid_pc;
    }

    public void setNum_customer_paid_pc(int num_customer_paid_pc) {
        this.num_customer_paid_pc = num_customer_paid_pc;
    }

    public int getQty_case_paid_pc() {
        return qty_case_paid_pc;
    }

    public void setQty_case_paid_pc(int qty_case_paid_pc) {
        this.qty_case_paid_pc = qty_case_paid_pc;
    }

    public int getQty_product_paid_pc() {
        return qty_product_paid_pc;
    }

    public void setQty_product_paid_pc(int qty_product_paid_pc) {
        this.qty_product_paid_pc = qty_product_paid_pc;
    }

    public BigDecimal getCvr_paid_pc() {
        return cvr_paid_pc;
    }

    public void setCvr_paid_pc(BigDecimal cvr_paid_pc) {
        this.cvr_paid_pc = cvr_paid_pc;
    }

    public int getNum_c_order_paid_pc() {
        return num_c_order_paid_pc;
    }

    public void setNum_c_order_paid_pc(int num_c_order_paid_pc) {
        this.num_c_order_paid_pc = num_c_order_paid_pc;
    }

    public BigDecimal getCvr_paid_detail_pc() {
        return cvr_paid_detail_pc;
    }

    public void setCvr_paid_detail_pc(BigDecimal cvr_paid_detail_pc) {
        this.cvr_paid_detail_pc = cvr_paid_detail_pc;
    }

    public int getNum_f_order() {
        return num_f_order;
    }

    public void setNum_f_order(int num_f_order) {
        this.num_f_order = num_f_order;
    }

    public BigDecimal getAmt_order() {
        return amt_order;
    }

    public void setAmt_order(BigDecimal amt_order) {
        this.amt_order = amt_order;
    }

    public int getNum_customer() {
        return num_customer;
    }

    public void setNum_customer(int num_customer) {
        this.num_customer = num_customer;
    }

    public int getNum_f_order_paid() {
        return num_f_order_paid;
    }

    public void setNum_f_order_paid(int num_f_order_paid) {
        this.num_f_order_paid = num_f_order_paid;
    }

    public BigDecimal getAmt_order_paid() {
        return amt_order_paid;
    }

    public void setAmt_order_paid(BigDecimal amt_order_paid) {
        this.amt_order_paid = amt_order_paid;
    }

    public int getNum_customer_order_paid() {
        return num_customer_order_paid;
    }

    public void setNum_customer_order_paid(int num_customer_order_paid) {
        this.num_customer_order_paid = num_customer_order_paid;
    }

    public int getNum_case_order_paid() {
        return num_case_order_paid;
    }

    public void setNum_case_order_paid(int num_case_order_paid) {
        this.num_case_order_paid = num_case_order_paid;
    }

    public int getNum_case_order() {
        return num_case_order;
    }

    public void setNum_case_order(int num_case_order) {
        this.num_case_order = num_case_order;
    }

    public BigDecimal getCvr_order() {
        return cvr_order;
    }

    public void setCvr_order(BigDecimal cvr_order) {
        this.cvr_order = cvr_order;
    }

    public int getNum_c_order() {
        return num_c_order;
    }

    public void setNum_c_order(int num_c_order) {
        this.num_c_order = num_c_order;
    }

    public int getNum_new_customer() {
        return num_new_customer;
    }

    public void setNum_new_customer(int num_new_customer) {
        this.num_new_customer = num_new_customer;
    }

    public BigDecimal getAvg_case_paid() {
        return avg_case_paid;
    }

    public void setAvg_case_paid(BigDecimal avg_case_paid) {
        this.avg_case_paid = avg_case_paid;
    }

    public BigDecimal getAvg_num_c_order_paid() {
        return avg_num_c_order_paid;
    }

    public void setAvg_num_c_order_paid(BigDecimal avg_num_c_order_paid) {
        this.avg_num_c_order_paid = avg_num_c_order_paid;
    }

    public int getNum_f_paid() {
        return num_f_paid;
    }

    public void setNum_f_paid(int num_f_paid) {
        this.num_f_paid = num_f_paid;
    }

    public BigDecimal getAmt_paid() {
        return amt_paid;
    }

    public void setAmt_paid(BigDecimal amt_paid) {
        this.amt_paid = amt_paid;
    }

    public BigDecimal getAmt_paid_change_daily() {
        return amt_paid_change_daily;
    }

    public void setAmt_paid_change_daily(BigDecimal amt_paid_change_daily) {
        this.amt_paid_change_daily = amt_paid_change_daily;
    }

    public int getNum_customer_paid() {
        return num_customer_paid;
    }

    public void setNum_customer_paid(int num_customer_paid) {
        this.num_customer_paid = num_customer_paid;
    }

    public int getNum_case_paid() {
        return num_case_paid;
    }

    public void setNum_case_paid(int num_case_paid) {
        this.num_case_paid = num_case_paid;
    }

    public int getNum_product_paid() {
        return num_product_paid;
    }

    public void setNum_product_paid(int num_product_paid) {
        this.num_product_paid = num_product_paid;
    }

    public BigDecimal getAvg_paid() {
        return avg_paid;
    }

    public void setAvg_paid(BigDecimal avg_paid) {
        this.avg_paid = avg_paid;
    }

    public int getNum_c_order_paid() {
        return num_c_order_paid;
    }

    public void setNum_c_order_paid(int num_c_order_paid) {
        this.num_c_order_paid = num_c_order_paid;
    }

    public BigDecimal getAmt_paid_tb_app() {
        return amt_paid_tb_app;
    }

    public void setAmt_paid_tb_app(BigDecimal amt_paid_tb_app) {
        this.amt_paid_tb_app = amt_paid_tb_app;
    }

    public int getNum_customer_paid_tb_app() {
        return num_customer_paid_tb_app;
    }

    public void setNum_customer_paid_tb_app(int num_customer_paid_tb_app) {
        this.num_customer_paid_tb_app = num_customer_paid_tb_app;
    }

    public int getNum_case_paid_tb_app() {
        return num_case_paid_tb_app;
    }

    public void setNum_case_paid_tb_app(int num_case_paid_tb_app) {
        this.num_case_paid_tb_app = num_case_paid_tb_app;
    }

    public BigDecimal getCvr_paid_tb_app() {
        return cvr_paid_tb_app;
    }

    public void setCvr_paid_tb_app(BigDecimal cvr_paid_tb_app) {
        this.cvr_paid_tb_app = cvr_paid_tb_app;
    }

    public BigDecimal getAmt_paid_tm_app() {
        return amt_paid_tm_app;
    }

    public void setAmt_paid_tm_app(BigDecimal amt_paid_tm_app) {
        this.amt_paid_tm_app = amt_paid_tm_app;
    }

    public int getNum_customer_paid_tm_app() {
        return num_customer_paid_tm_app;
    }

    public void setNum_customer_paid_tm_app(int num_customer_paid_tm_app) {
        this.num_customer_paid_tm_app = num_customer_paid_tm_app;
    }

    public int getNum_case_paid_tm_app() {
        return num_case_paid_tm_app;
    }

    public void setNum_case_paid_tm_app(int num_case_paid_tm_app) {
        this.num_case_paid_tm_app = num_case_paid_tm_app;
    }

    public BigDecimal getCvr_paid_tm_app() {
        return cvr_paid_tm_app;
    }

    public void setCvr_paid_tm_app(BigDecimal cvr_paid_tm_app) {
        this.cvr_paid_tm_app = cvr_paid_tm_app;
    }

    public BigDecimal getAmt_paid_wap() {
        return amt_paid_wap;
    }

    public void setAmt_paid_wap(BigDecimal amt_paid_wap) {
        this.amt_paid_wap = amt_paid_wap;
    }

    public int getNum_customer_paid_wap() {
        return num_customer_paid_wap;
    }

    public void setNum_customer_paid_wap(int num_customer_paid_wap) {
        this.num_customer_paid_wap = num_customer_paid_wap;
    }

    public int getNum_case_paid_wap() {
        return num_case_paid_wap;
    }

    public void setNum_case_paid_wap(int num_case_paid_wap) {
        this.num_case_paid_wap = num_case_paid_wap;
    }

    public BigDecimal getCvr_paid_wap() {
        return cvr_paid_wap;
    }

    public void setCvr_paid_wap(BigDecimal cvr_paid_wap) {
        this.cvr_paid_wap = cvr_paid_wap;
    }

    public BigDecimal getPrice_customer_unit_wireless() {
        return price_customer_unit_wireless;
    }

    public void setPrice_customer_unit_wireless(BigDecimal price_customer_unit_wireless) {
        this.price_customer_unit_wireless = price_customer_unit_wireless;
    }

    public BigDecimal getAmt_order_wireless() {
        return amt_order_wireless;
    }

    public void setAmt_order_wireless(BigDecimal amt_order_wireless) {
        this.amt_order_wireless = amt_order_wireless;
    }

    public int getNum_customer_order_wireless() {
        return num_customer_order_wireless;
    }

    public void setNum_customer_order_wireless(int num_customer_order_wireless) {
        this.num_customer_order_wireless = num_customer_order_wireless;
    }

    public BigDecimal getCvr_order_wireless() {
        return cvr_order_wireless;
    }

    public void setCvr_order_wireless(BigDecimal cvr_order_wireless) {
        this.cvr_order_wireless = cvr_order_wireless;
    }

    public int getNum_f_order_paid_wireless() {
        return num_f_order_paid_wireless;
    }

    public void setNum_f_order_paid_wireless(int num_f_order_paid_wireless) {
        this.num_f_order_paid_wireless = num_f_order_paid_wireless;
    }

    public BigDecimal getAmt_paid_wireless() {
        return amt_paid_wireless;
    }

    public void setAmt_paid_wireless(BigDecimal amt_paid_wireless) {
        this.amt_paid_wireless = amt_paid_wireless;
    }

    public BigDecimal getAmt_rate_paid_wireless() {
        return amt_rate_paid_wireless;
    }

    public void setAmt_rate_paid_wireless(BigDecimal amt_rate_paid_wireless) {
        this.amt_rate_paid_wireless = amt_rate_paid_wireless;
    }

    public int getNum_regular_paid_wireless() {
        return num_regular_paid_wireless;
    }

    public void setNum_regular_paid_wireless(int num_regular_paid_wireless) {
        this.num_regular_paid_wireless = num_regular_paid_wireless;
    }

    public int getNum_customer_paid_wireless() {
        return num_customer_paid_wireless;
    }

    public void setNum_customer_paid_wireless(int num_customer_paid_wireless) {
        this.num_customer_paid_wireless = num_customer_paid_wireless;
    }

    public BigDecimal getNum_rate_customer_paid_wireless() {
        return num_rate_customer_paid_wireless;
    }

    public void setNum_rate_customer_paid_wireless(BigDecimal num_rate_customer_paid_wireless) {
        this.num_rate_customer_paid_wireless = num_rate_customer_paid_wireless;
    }

    public int getNum_case_paid_wireless() {
        return num_case_paid_wireless;
    }

    public void setNum_case_paid_wireless(int num_case_paid_wireless) {
        this.num_case_paid_wireless = num_case_paid_wireless;
    }

    public int getNum_product_paid_wireless() {
        return num_product_paid_wireless;
    }

    public void setNum_product_paid_wireless(int num_product_paid_wireless) {
        this.num_product_paid_wireless = num_product_paid_wireless;
    }

    public BigDecimal getCvr_paid_wireless() {
        return cvr_paid_wireless;
    }

    public void setCvr_paid_wireless(BigDecimal cvr_paid_wireless) {
        this.cvr_paid_wireless = cvr_paid_wireless;
    }

    public int getNum_c_order_paid_wireless() {
        return num_c_order_paid_wireless;
    }

    public void setNum_c_order_paid_wireless(int num_c_order_paid_wireless) {
        this.num_c_order_paid_wireless = num_c_order_paid_wireless;
    }

    public int getNum_customer_low_dsr_com() {
        return num_customer_low_dsr_com;
    }

    public void setNum_customer_low_dsr_com(int num_customer_low_dsr_com) {
        this.num_customer_low_dsr_com = num_customer_low_dsr_com;
    }

    public BigDecimal getDsr_service() {
        return dsr_service;
    }

    public void setDsr_service(BigDecimal dsr_service) {
        this.dsr_service = dsr_service;
    }

    public BigDecimal getDsr_describe() {
        return dsr_describe;
    }

    public void setDsr_describe(BigDecimal dsr_describe) {
        this.dsr_describe = dsr_describe;
    }

    public BigDecimal getAmt_return() {
        return amt_return;
    }

    public void setAmt_return(BigDecimal amt_return) {
        this.amt_return = amt_return;
    }

    public int getNum_customer_return() {
        return num_customer_return;
    }

    public void setNum_customer_return(int num_customer_return) {
        this.num_customer_return = num_customer_return;
    }

    public BigDecimal getDsr_express() {
        return dsr_express;
    }

    public void setDsr_express(BigDecimal dsr_express) {
        this.dsr_express = dsr_express;
    }

    public int getNum_f_order_shipped() {
        return num_f_order_shipped;
    }

    public void setNum_f_order_shipped(int num_f_order_shipped) {
        this.num_f_order_shipped = num_f_order_shipped;
    }

    public int getTimes_collect_store() {
        return times_collect_store;
    }

    public void setTimes_collect_store(int times_collect_store) {
        this.times_collect_store = times_collect_store;
    }

    public int getTimes_collect_product() {
        return times_collect_product;
    }

    public void setTimes_collect_product(int times_collect_product) {
        this.times_collect_product = times_collect_product;
    }

    public int getNum_collect_customer_product() {
        return num_collect_customer_product;
    }

    public void setNum_collect_customer_product(int num_collect_customer_product) {
        this.num_collect_customer_product = num_collect_customer_product;
    }

    public int getNum_collect_customer_store() {
        return num_collect_customer_store;
    }

    public void setNum_collect_customer_store(int num_collect_customer_store) {
        this.num_collect_customer_store = num_collect_customer_store;
    }
}
