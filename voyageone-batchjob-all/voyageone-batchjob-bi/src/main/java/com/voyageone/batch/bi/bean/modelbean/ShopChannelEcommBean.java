package com.voyageone.batch.bi.bean.modelbean;


/**
 * Created by Kylin on 2015/7/17.
 */
public class ShopChannelEcommBean {
    private int shop_id;
    private String shop_code;
    private int order_channel_id;
    private String order_channel_code;
    private int ecomm_id;
    private String ecomm_code;
    private String user_name;
    private String user_ps;
    private String login_url;
    private String reflash_url;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_code() {
        return shop_code;
    }

    public void setShop_code(String shop_code) {
        this.shop_code = shop_code;
    }

    public int getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(int order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getOrder_channel_code() {
        return order_channel_code;
    }

    public void setOrder_channel_code(String order_channel_code) {
        this.order_channel_code = order_channel_code;
    }

    public int getEcomm_id() {
        return ecomm_id;
    }

    public void setEcomm_id(int ecomm_id) {
        this.ecomm_id = ecomm_id;
    }

    public String getEcomm_code() {
        return ecomm_code;
    }

    public void setEcomm_code(String ecomm_code) {
        this.ecomm_code = ecomm_code;
    }

    public String getLogin_url() {
        return login_url;
    }

    public void setLogin_url(String login_url) {
        this.login_url = login_url;
    }

    public String getReflash_url() {
        return reflash_url;
    }

    public void setReflash_url(String reflash_url) {
        this.reflash_url = reflash_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_ps() {
        return user_ps;
    }

    public void setUser_ps(String user_ps) {
        this.user_ps = user_ps;
    }
}
