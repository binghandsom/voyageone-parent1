package com.voyageone.common.configs.beans;

/**
 * Created by Jonas on 4/13/2015.
 */
public class OrderChannelBean {
    private String order_channel_id;

    private String company_id;

    private String name;

    private String full_name;

    private String send_name;

    private String send_address;

    private String send_tel;

    private String send_zip;

    private String screct_key;

    private String session_key;

    private Integer is_usjoi;

    private String cart_ids;

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSend_name() {
        return send_name;
    }

    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }

    public String getSend_address() {
        return send_address;
    }

    public void setSend_address(String send_address) {
        this.send_address = send_address;
    }

    public String getSend_tel() {
        return send_tel;
    }

    public void setSend_tel(String send_tel) {
        this.send_tel = send_tel;
    }

    public String getSend_zip() {
        return send_zip;
    }

    public void setSend_zip(String send_zip) {
        this.send_zip = send_zip;
    }

    public String getScrect_key() {
        return screct_key;
    }

    public void setScrect_key(String screct_key) {
        this.screct_key = screct_key;
    }

    public Integer getIs_usjoi() {
        return is_usjoi;
    }

    public void setIs_usjoi(Integer is_usjoi) {
        this.is_usjoi = is_usjoi;
    }

    public String getCart_ids() {
        return cart_ids;
    }

    public void setCart_ids(String cart_ids) {
        this.cart_ids = cart_ids;
    }
}
