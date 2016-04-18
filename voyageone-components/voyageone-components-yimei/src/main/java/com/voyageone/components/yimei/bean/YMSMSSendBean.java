package com.voyageone.components.yimei.bean;

/**
 * Created by Fred on 2015/8/28.
 */
public class YMSMSSendBean {
    //    电话号码
    private String phone;
    //    销售渠道
    private String order_channel_id;
    //    短信内容
    private String content;

    // 短信类型
    private String sms_type;

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSms_type() {
        return sms_type;
    }

    public void setSms_type(String sms_type) {
        this.sms_type = sms_type;
    }
}
