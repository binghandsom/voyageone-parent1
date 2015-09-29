package com.voyageone.batch.synship.modelbean;

/**
 * 映射 tm_sms_config 表
 *
 * Created by Jonas on 9/23/15.
 */
public class SmsConfigBean {
    // 用户渠道
    private String order_channel_id;
    // 短信类型
    private String sms_type;
    // 订单状态
    private String sms_code1;
    // 发货渠道
    private String sms_code2;
    // 短信内容
    private String content;
    // 短信描述
    private String describe;
    // 当前可用判断
    private String del_flg;

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getSms_type() {
        return sms_type;
    }

    public void setSms_type(String sms_type) {
        this.sms_type = sms_type;
    }

    public String getSms_code1() {
        return sms_code1;
    }

    public void setSms_code1(String sms_code1) {
        this.sms_code1 = sms_code1;
    }

    public String getSms_code2() {
        return sms_code2;
    }

    public void setSms_code2(String sms_code2) {
        this.sms_code2 = sms_code2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDel_flg() {
        return del_flg;
    }

    public void setDel_flg(String del_flg) {
        this.del_flg = del_flg;
    }
}
