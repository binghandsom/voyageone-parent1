package com.voyageone.bi.bean;

import java.math.BigDecimal;

/**
 * Created by Kylin on 2015/7/21.
 */
public class BillBean {

    private int seq;
    private int file_type;
    private String file_name;
    private String document_path;
    private String file_upload_date;
    private String invoice_num;
    private BigDecimal transpotation_fee;
    private BigDecimal ground_handling_fee;
    private BigDecimal storage_charges;
    private BigDecimal mail_fee;
    private BigDecimal identification_fee;
    private BigDecimal tax;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getFile_type() {
        return file_type;
    }

    public void setFile_type(int file_type) {
        this.file_type = file_type;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getDocument_path() {
        return document_path;
    }

    public void setDocument_path(String document_path) {
        this.document_path = document_path;
    }

    public String getFile_upload_date() {
        return file_upload_date;
    }

    public void setFile_upload_date(String file_upload_date) {
        this.file_upload_date = file_upload_date;
    }

    public String getInvoice_num() {
        return invoice_num;
    }

    public void setInvoice_num(String invoice_num) {
        this.invoice_num = invoice_num;
    }

    public BigDecimal getTranspotation_fee() {
        return transpotation_fee;
    }

    public void setTranspotation_fee(BigDecimal transpotation_fee) {
        this.transpotation_fee = transpotation_fee;
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

    public BigDecimal getMail_fee() {
        return mail_fee;
    }

    public void setMail_fee(BigDecimal mail_fee) {
        this.mail_fee = mail_fee;
    }

    public BigDecimal getIdentification_fee() {
        return identification_fee;
    }

    public void setIdentification_fee(BigDecimal identification_fee) {
        this.identification_fee = identification_fee;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
