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

    private String status;

    private String sent_kd100_poll_flg;

    private String tracking_no;

    private String tracking_type;

    private String tracking_status;

    private String before_status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSent_kd100_poll_flg() {
        return sent_kd100_poll_flg;
    }

    public void setSent_kd100_poll_flg(String sent_kd100_poll_flg) {
        this.sent_kd100_poll_flg = sent_kd100_poll_flg;
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

    public String getTracking_status() {
        return tracking_status;
    }

    public void setTracking_status(String tracking_status) {
        this.tracking_status = tracking_status;
    }

    public String getBefore_status() {
        return before_status;
    }

    public void setBefore_status(String before_status) {
        this.before_status = before_status;
    }
}
