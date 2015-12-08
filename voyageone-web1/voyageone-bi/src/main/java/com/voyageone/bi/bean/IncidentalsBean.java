package com.voyageone.bi.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Kylin on 2015/7/21.
 */
public class IncidentalsBean {

    private int seq;
    private String invoice_num;
    private Date arrived_time;
    private Date process_time;
    private String main_waybill_num;
    private BigDecimal weight_actual_kg;
    private BigDecimal weight_calc_kg;
    private BigDecimal ground_handling_fee;
    private BigDecimal storage_charges;
    private BigDecimal amount;
    private String notes;
    private int residence_days;

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

    public Date getArrived_time() {
        return arrived_time;
    }

    public void setArrived_time(Date arrived_time) {
        this.arrived_time = arrived_time;
    }

    public Date getProcess_time() {
        return process_time;
    }

    public void setProcess_time(Date process_time) {
        this.process_time = process_time;
    }

    public String getMain_waybill_num() {
        return main_waybill_num;
    }

    public void setMain_waybill_num(String main_waybill_num) {
        this.main_waybill_num = main_waybill_num;
    }

    public BigDecimal getWeight_actual_kg() {
        return weight_actual_kg;
    }

    public void setWeight_actual_kg(BigDecimal weight_actual_kg) {
        this.weight_actual_kg = weight_actual_kg;
    }

    public BigDecimal getWeight_calc_kg() {
        return weight_calc_kg;
    }

    public void setWeight_calc_kg(BigDecimal weight_calc_kg) {
        this.weight_calc_kg = weight_calc_kg;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getResidence_days() {
        return residence_days;
    }

    public void setResidence_days(int residence_days) {
        this.residence_days = residence_days;
    }
}
