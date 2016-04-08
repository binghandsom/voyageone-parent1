package com.voyageone.components.jumei.bean;

/**
 * Created by sn3 on 2015-07-18.
 */
public class SetShippingReq extends JmBaseBean {

    private String order_id;
    private String logistic_id;
    private String logistic_track_no;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getLogistic_id() {
        return logistic_id;
    }

    public void setLogistic_id(String logistic_id) {
        this.logistic_id = logistic_id;
    }

    public String getLogistic_track_no() {
        return logistic_track_no;
    }

    public void setLogistic_track_no(String logistic_track_no) {
        this.logistic_track_no = logistic_track_no;
    }
}
