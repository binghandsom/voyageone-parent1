package com.voyageone.bi.bean;

/**
 * Created by Kylin on 2015/7/21.
 */
public class BillErrorBean {

    private String invoice_num;
    private String main_no;
    private String bill_related_no;
    private String db_related_no;
    private String error_type_id;
    private String error_description;

    public String getInvoice_num() {
        return invoice_num;
    }

    public void setInvoice_num(String invoice_num) {
        this.invoice_num = invoice_num;
    }

    public String getMain_no() {
        return main_no;
    }

    public void setMain_no(String main_no) {
        this.main_no = main_no;
    }

    public String getBill_related_no() {
        return bill_related_no;
    }

    public void setBill_related_no(String bill_related_no) {
        this.bill_related_no = bill_related_no;
    }

    public String getDb_related_no() {
        return db_related_no;
    }

    public void setDb_related_no(String db_related_no) {
        this.db_related_no = db_related_no;
    }

    public String getError_type_id() {
        return error_type_id;
    }

    public void setError_type_id(String error_type_id) {
        this.error_type_id = error_type_id;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
