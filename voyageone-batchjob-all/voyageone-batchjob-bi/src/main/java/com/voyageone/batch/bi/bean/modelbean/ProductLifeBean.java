package com.voyageone.batch.bi.bean.modelbean;

/**
 * Created by Kylin on 2015/7/8.
 */
public class ProductLifeBean {
    private int shop_id;

    private String product_id;

    private String product_code;

    private int online_date;

    private int offline_date;

    private int sales_first_date;

    private int sales_last_date;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getOnline_date() {
        return online_date;
    }

    public void setOnline_date(int online_date) {
        this.online_date = online_date;
    }

    public int getOffline_date() {
        return offline_date;
    }

    public void setOffline_date(int offline_date) {
        this.offline_date = offline_date;
    }

    public int getSales_first_date() {
        return sales_first_date;
    }

    public void setSales_first_date(int sales_first_date) {
        this.sales_first_date = sales_first_date;
    }

    public int getSales_last_date() {
        return sales_last_date;
    }

    public void setSales_last_date(int sales_last_date) {
        this.sales_last_date = sales_last_date;
    }
}
