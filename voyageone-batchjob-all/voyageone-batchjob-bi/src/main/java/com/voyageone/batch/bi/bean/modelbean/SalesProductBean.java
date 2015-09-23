package com.voyageone.batch.bi.bean.modelbean;

import java.math.BigDecimal;

/**
 * Created by Kylin on 2015/6/15.
 * vt_sales_product
 */
public class SalesProductBean {
    private int shop_id;
    private String num_iid;
    private int process_date;
    private int uv_pc;
    private int uv_mobile;
    private int uv;
    private int pv_pc;
    private int pv_mobile;
    private int pv;
    private BigDecimal bounce_rate_pc;
    private BigDecimal bounce_rate_mobile;
    private BigDecimal bounce_rate;
    private BigDecimal visit_time_avg_pc;
    private BigDecimal visit_time_avg_mobile;
    private BigDecimal visit_time_avg;
    private BigDecimal pay_cvr_pc;
    private BigDecimal pay_cvr_mobile;
    private BigDecimal order_cvr;
    private BigDecimal pay_cvr;
    private int product_cart_increment_pc;
    private int product_cart_increment_mobile;
    private int product_cart_increment;
    private int customer_cart_increment;
    private int product_collector_number;
    private int order_increment_pc;
    private int order_increment_mobile;
    private int order_increment;
    private int pay_product_qty_pc;
    private int pay_product_qty_mobile;
    private int pay_product_qty;
    private int pay_buyer_number_pc;
    private int pay_buyer_number_mobile;
    private int pay_buyer_number;
    private BigDecimal pay_amt_pc;
    private BigDecimal pay_amt_mobile;
    private BigDecimal pay_amt;
    private BigDecimal order_amt;
    private int order_buyer_number;
    private int order_product_number;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public int getProcess_date() {
        return process_date;
    }

    public void setProcess_date(int process_date) {
        this.process_date = process_date;
    }

    public int getUv_pc() {
        return uv_pc;
    }

    public void setUv_pc(int uv_pc) {
        this.uv_pc = uv_pc;
    }

    public int getUv_mobile() {
        return uv_mobile;
    }

    public void setUv_mobile(int uv_mobile) {
        this.uv_mobile = uv_mobile;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getPv_pc() {
        return pv_pc;
    }

    public void setPv_pc(int pv_pc) {
        this.pv_pc = pv_pc;
    }

    public int getPv_mobile() {
        return pv_mobile;
    }

    public void setPv_mobile(int pv_mobile) {
        this.pv_mobile = pv_mobile;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public BigDecimal getBounce_rate_pc() {
        return bounce_rate_pc;
    }

    public void setBounce_rate_pc(BigDecimal bounce_rate_pc) {
        this.bounce_rate_pc = bounce_rate_pc;
    }

    public BigDecimal getBounce_rate_mobile() {
        return bounce_rate_mobile;
    }

    public void setBounce_rate_mobile(BigDecimal bounce_rate_mobile) {
        this.bounce_rate_mobile = bounce_rate_mobile;
    }

    public BigDecimal getBounce_rate() {
        return bounce_rate;
    }

    public void setBounce_rate(BigDecimal bounce_rate) {
        this.bounce_rate = bounce_rate;
    }

    public BigDecimal getVisit_time_avg_pc() {
        return visit_time_avg_pc;
    }

    public void setVisit_time_avg_pc(BigDecimal visit_time_avg_pc) {
        this.visit_time_avg_pc = visit_time_avg_pc;
    }

    public BigDecimal getVisit_time_avg_mobile() {
        return visit_time_avg_mobile;
    }

    public void setVisit_time_avg_mobile(BigDecimal visit_time_avg_mobile) {
        this.visit_time_avg_mobile = visit_time_avg_mobile;
    }

    public BigDecimal getVisit_time_avg() {
        return visit_time_avg;
    }

    public void setVisit_time_avg(BigDecimal visit_time_avg) {
        this.visit_time_avg = visit_time_avg;
    }

    public BigDecimal getPay_cvr_pc() {
        return pay_cvr_pc;
    }

    public void setPay_cvr_pc(BigDecimal pay_cvr_pc) {
        this.pay_cvr_pc = pay_cvr_pc;
    }

    public BigDecimal getPay_cvr_mobile() {
        return pay_cvr_mobile;
    }

    public void setPay_cvr_mobile(BigDecimal pay_cvr_mobile) {
        this.pay_cvr_mobile = pay_cvr_mobile;
    }

    public BigDecimal getOrder_cvr() {
        return order_cvr;
    }

    public void setOrder_cvr(BigDecimal order_cvr) {
        this.order_cvr = order_cvr;
    }

    public BigDecimal getPay_cvr() {
        return pay_cvr;
    }

    public void setPay_cvr(BigDecimal pay_cvr) {
        this.pay_cvr = pay_cvr;
    }

    public int getProduct_cart_increment_pc() {
        return product_cart_increment_pc;
    }

    public void setProduct_cart_increment_pc(int product_cart_increment_pc) {
        this.product_cart_increment_pc = product_cart_increment_pc;
    }

    public int getProduct_cart_increment_mobile() {
        return product_cart_increment_mobile;
    }

    public void setProduct_cart_increment_mobile(int product_cart_increment_mobile) {
        this.product_cart_increment_mobile = product_cart_increment_mobile;
    }

    public int getProduct_cart_increment() {
        return product_cart_increment;
    }

    public void setProduct_cart_increment(int product_cart_increment) {
        this.product_cart_increment = product_cart_increment;
    }

    public int getCustomer_cart_increment() {
        return customer_cart_increment;
    }

    public void setCustomer_cart_increment(int customer_cart_increment) {
        this.customer_cart_increment = customer_cart_increment;
    }

    public int getProduct_collector_number() {
        return product_collector_number;
    }

    public void setProduct_collector_number(int product_collector_number) {
        this.product_collector_number = product_collector_number;
    }

    public int getOrder_increment_pc() {
        return order_increment_pc;
    }

    public void setOrder_increment_pc(int order_increment_pc) {
        this.order_increment_pc = order_increment_pc;
    }

    public int getOrder_increment_mobile() {
        return order_increment_mobile;
    }

    public void setOrder_increment_mobile(int order_increment_mobile) {
        this.order_increment_mobile = order_increment_mobile;
    }

    public int getOrder_increment() {
        return order_increment;
    }

    public void setOrder_increment(int order_increment) {
        this.order_increment = order_increment;
    }

    public int getPay_product_qty_pc() {
        return pay_product_qty_pc;
    }

    public void setPay_product_qty_pc(int pay_product_qty_pc) {
        this.pay_product_qty_pc = pay_product_qty_pc;
    }

    public int getPay_product_qty_mobile() {
        return pay_product_qty_mobile;
    }

    public void setPay_product_qty_mobile(int pay_product_qty_mobile) {
        this.pay_product_qty_mobile = pay_product_qty_mobile;
    }

    public int getPay_product_qty() {
        return pay_product_qty;
    }

    public void setPay_product_qty(int pay_product_qty) {
        this.pay_product_qty = pay_product_qty;
    }

    public int getPay_buyer_number_pc() {
        return pay_buyer_number_pc;
    }

    public void setPay_buyer_number_pc(int pay_buyer_number_pc) {
        this.pay_buyer_number_pc = pay_buyer_number_pc;
    }

    public int getPay_buyer_number_mobile() {
        return pay_buyer_number_mobile;
    }

    public void setPay_buyer_number_mobile(int pay_buyer_number_mobile) {
        this.pay_buyer_number_mobile = pay_buyer_number_mobile;
    }

    public int getPay_buyer_number() {
        return pay_buyer_number;
    }

    public void setPay_buyer_number(int pay_buyer_number) {
        this.pay_buyer_number = pay_buyer_number;
    }

    public BigDecimal getPay_amt_pc() {
        return pay_amt_pc;
    }

    public void setPay_amt_pc(BigDecimal pay_amt_pc) {
        this.pay_amt_pc = pay_amt_pc;
    }

    public BigDecimal getPay_amt_mobile() {
        return pay_amt_mobile;
    }

    public void setPay_amt_mobile(BigDecimal pay_amt_mobile) {
        this.pay_amt_mobile = pay_amt_mobile;
    }

    public BigDecimal getPay_amt() {
        return pay_amt;
    }

    public void setPay_amt(BigDecimal pay_amt) {
        this.pay_amt = pay_amt;
    }

    public BigDecimal getOrder_amt() {
        return order_amt;
    }

    public void setOrder_amt(BigDecimal order_amt) {
        this.order_amt = order_amt;
    }

    public int getOrder_buyer_number() {
        return order_buyer_number;
    }

    public void setOrder_buyer_number(int order_buyer_number) {
        this.order_buyer_number = order_buyer_number;
    }

    public int getOrder_product_number() {
        return order_product_number;
    }

    public void setOrder_product_number(int order_product_number) {
        this.order_product_number = order_product_number;
    }
}
