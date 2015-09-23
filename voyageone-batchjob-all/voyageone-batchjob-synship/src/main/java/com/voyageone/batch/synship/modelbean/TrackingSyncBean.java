package com.voyageone.batch.synship.modelbean;

/**
 * 获取的需要同步的运单
 *
 * Created by jack on 15/8/2.
 */
public class TrackingSyncBean {

    private String syn_ship_no;

    private long order_number;

    private String source_order_id;

    private String order_channel_id;

    private int cart_id;

    private String send_tracking_flg;

    private String update_time;

    private String update_person;

    private String tracking_info;

    private String tracking_no;

    private String tracking_type;

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

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
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

    public String getTracking_info() {
        return tracking_info;
    }

    public void setTracking_info(String tracking_info) {
        this.tracking_info = tracking_info;
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
}
