package com.voyageone.batch.bi.bean.modelbean.jumei;

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
}
