package com.voyageone.batch.synship.modelbean;

/**
 * 获取的需要轮询的运单
 *
 * Created by jack on 15/9/27.
 */
public class EtkTrackingBean {

    private String syn_ship_no;

    private long order_number;

    private String order_channel_id;

    private int status;

    private String send_tracking_flg;

    private String update_time;

    private String update_person;

    private String tracking_no;

    private String tracking_type;

    private String sent_kd100_poll_flg;

    public String getSyn_ship_no() {
        return syn_ship_no;
    }

    public void setSyn_ship_no(String syn_ship_no) {
        this.syn_ship_no = syn_ship_no;
    }

    public long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(long order_number) {
        this.order_number = order_number;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSend_tracking_flg() {
        return send_tracking_flg;
    }

    public void setSend_tracking_flg(String send_tracking_flg) {
        this.send_tracking_flg = send_tracking_flg;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_person() {
        return update_person;
    }

    public void setUpdate_person(String update_person) {
        this.update_person = update_person;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public String getTracking_type() {
        return tracking_type;
    }

    public void setTracking_type(String tracking_type) {
        this.tracking_type = tracking_type;
    }

    public String getSent_kd100_poll_flg() {
        return sent_kd100_poll_flg;
    }

    public void setSent_kd100_poll_flg(String sent_kd100_poll_flg) {
        this.sent_kd100_poll_flg = sent_kd100_poll_flg;
    }
}
