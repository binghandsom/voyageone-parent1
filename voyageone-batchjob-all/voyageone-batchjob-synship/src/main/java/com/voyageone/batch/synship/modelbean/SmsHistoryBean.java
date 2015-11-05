package com.voyageone.batch.synship.modelbean;

/**
 * Created by Fred on 2015/8/31.
 */
public class SmsHistoryBean {

    // 自动连番
    private String auto_no;
    // 网络订单号
    private String source_order_id;
    // 收件人电话
    private String ship_phone;
    // 收件人姓名
    private String ship_name;
    // 发送类型
    private String sent_type;
    // 发送者
    private String sent_person;
    // 发送时间
    private String sent_time;
    // 发送内容
    private String sent_conent;
    // 发送费用
    private String sent_cost;
    // 到货口岸
    private String port;
    // 状态
    private String status;
    // 发送标志位
    private String sms_flg;
    // 短信类型
    private String sms_type;
    // 销售渠道
    private String order_channel_id;
    // 任务编号
    private String task_id;
    // 做成时间
    private String create_time;
    // 更新时间
    private String update_time;
    // 做成者
    private String create_person;
    // 更新者
    private String update_person;


    public String getAuto_no() {
        return auto_no;
    }

    public void setAuto_no(String auto_no) {
        this.auto_no = auto_no;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getShip_phone() {
        return ship_phone;
    }

    public void setShip_phone(String ship_phone) {
        this.ship_phone = ship_phone;
    }

    public String getShip_name() {
        return ship_name;
    }

    public void setShip_name(String ship_name) {
        this.ship_name = ship_name;
    }

    public String getSent_type() {
        return sent_type;
    }

    public void setSent_type(String sent_type) {
        this.sent_type = sent_type;
    }

    public String getSent_person() {
        return sent_person;
    }

    public void setSent_person(String sent_person) {
        this.sent_person = sent_person;
    }

    public String getSent_time() {
        return sent_time;
    }

    public void setSent_time(String sent_time) {
        this.sent_time = sent_time;
    }

    public String getSent_conent() {
        return sent_conent;
    }

    public void setSent_conent(String sent_conent) {
        this.sent_conent = sent_conent;
    }

    public String getSent_cost() {
        return sent_cost;
    }

    public void setSent_cost(String sent_cost) {
        this.sent_cost = sent_cost;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSms_flg() {
        return sms_flg;
    }

    public void setSms_flg(String sms_flg) {
        this.sms_flg = sms_flg;
    }

    public String getSms_type() {
        return sms_type;
    }

    public void setSms_type(String sms_type) {
        this.sms_type = sms_type;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_person() {
        return create_person;
    }

    public void setCreate_person(String create_person) {
        this.create_person = create_person;
    }

    public String getUpdate_person() {
        return update_person;
    }

    public void setUpdate_person(String update_person) {
        this.update_person = update_person;
    }
}
