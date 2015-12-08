package com.voyageone.bi.bean;

import java.math.BigDecimal;

/**
 * Created by Kylin on 2015/7/21.
 */
public class TaxDetailBean {

    private int seq;
    private String invoice_num;
    private String main_waybill_num;
    private String tax_list_number;
    private String tracking_no;
    private BigDecimal tax_actual;

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

    public String getMain_waybill_num() {
        return main_waybill_num;
    }

    public void setMain_waybill_num(String main_waybill_num) {
        this.main_waybill_num = main_waybill_num;
    }

    public String getTax_list_number() {
        return tax_list_number;
    }

    public void setTax_list_number(String tax_list_number) {
        this.tax_list_number = tax_list_number;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public BigDecimal getTax_actual() {
        return tax_actual;
    }

    public void setTax_actual(BigDecimal tax_actual) {
        this.tax_actual = tax_actual;
    }
}
