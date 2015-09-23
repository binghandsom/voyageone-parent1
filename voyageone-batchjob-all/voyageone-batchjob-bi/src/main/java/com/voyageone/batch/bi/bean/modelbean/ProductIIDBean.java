package com.voyageone.batch.bi.bean.modelbean;

/**
 * Created by Kylin on 2015/6/4.
 */
public class ProductIIDBean {
    //通道ID
    private int channel_id;
    //平台ID
    private int ecomm_id;
    //店铺ID
    private int shop_id;
    //统计日期
    private String product_code;
    //店铺中商品编号
    private String num_iid;
    //PC端访客数
    private int update_flag;
    //无线端访客数
    private int update_time;

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public int getEcomm_id() {
        return ecomm_id;
    }

    public void setEcomm_id(int ecomm_id) {
        this.ecomm_id = ecomm_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public int getUpdate_flag() {
        return update_flag;
    }

    public void setUpdate_flag(int update_flag) {
        this.update_flag = update_flag;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }
}
