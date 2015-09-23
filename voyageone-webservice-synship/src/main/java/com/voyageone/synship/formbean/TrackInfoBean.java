package com.voyageone.synship.formbean;

import com.voyageone.synship.modelbean.TrackingInfoConfigBean;

/**
 * Created by dell on 2015/7/23.
 */
public class TrackInfoBean extends TrackingInfoConfigBean {

    // 物流订单号
    private String syn_ship_no;
    // 操作时间
    private String process_time;
    // 跟踪信息
    private String tracking_event;
    // 快递公司
    private String tracking_type;
    // 快递单号
    private String tracking_no;

    public String getSyn_ship_no() {
        return syn_ship_no;
    }

    public void setSyn_ship_no(String syn_ship_no) {
        this.syn_ship_no = syn_ship_no;
    }

    public String getProcess_time() {
        return process_time;
    }

    public void setProcess_time(String process_time) {
        this.process_time = process_time;
    }

    public String getTracking_event() {
        return tracking_event;
    }

    public void setTracking_event(String tracking_event) {
        this.tracking_event = tracking_event;
    }

    public String getTracking_type() {
        return tracking_type;
    }

    public void setTracking_type(String tracking_type) {
        this.tracking_type = tracking_type;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }
}
