package com.voyageone.components.jumei.bean;

/**
 * Created by sn3 on 2015-07-18.
 */
public class Refund_Info extends JmBaseBean {

    private String refund_id;
    private String refund_status;

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }
}
