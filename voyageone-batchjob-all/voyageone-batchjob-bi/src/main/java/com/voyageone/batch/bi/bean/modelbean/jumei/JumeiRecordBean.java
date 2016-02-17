package com.voyageone.batch.bi.bean.modelbean.jumei;

import java.sql.Timestamp;

/**
 * Created by Kylin on 2015/9/2.
 */
public class JumeiRecordBean {

    private String channel_id;
    private String task_id;
    private String product_code;
    private String jumei_product_name_cn_real;
    private int error_type_id;
    private String error_message;
    private Timestamp update_time;
    private Timestamp create_time;
    private int del_flg;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getJumei_product_name_cn_real() {
        return jumei_product_name_cn_real;
    }

    public void setJumei_product_name_cn_real(String jumei_product_name_cn_real) {
        this.jumei_product_name_cn_real = jumei_product_name_cn_real;
    }

    public int getError_type_id() {
        return error_type_id;
    }

    public void setError_type_id(int error_type_id) {
        this.error_type_id = error_type_id;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public int getDel_flg() {
        return del_flg;
    }

    public void setDel_flg(int del_flg) {
        this.del_flg = del_flg;
    }
}
