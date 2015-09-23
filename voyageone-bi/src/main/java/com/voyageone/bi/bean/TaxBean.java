package com.voyageone.bi.bean;

import java.math.BigDecimal;

/**
 * Created by Kylin on 2015/7/21.
 */
public class TaxBean {
    private int seq;
    private String invoice_num;
    private String pay_in_warrant_num;
    private String main_waybill_num;
    private int tax_list_qty;
    private BigDecimal tax_actual_amt;
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

    public String getPay_in_warrant_num() {
        return pay_in_warrant_num;
    }

    public void setPay_in_warrant_num(String pay_in_warrant_num) {
        this.pay_in_warrant_num = pay_in_warrant_num;
    }

    public String getMain_waybill_num() {
        return main_waybill_num;
    }

    public void setMain_waybill_num(String main_waybill_num) {
        this.main_waybill_num = main_waybill_num;
    }

    public int getTax_list_qty() {
        return tax_list_qty;
    }

    public void setTax_list_qty(int tax_list_qty) {
        this.tax_list_qty = tax_list_qty;
    }

    public BigDecimal getTax_actual_amt() {
        return tax_actual_amt;
    }

    public void setTax_actual_amt(BigDecimal tax_actual_amt) {
        this.tax_actual_amt = tax_actual_amt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
