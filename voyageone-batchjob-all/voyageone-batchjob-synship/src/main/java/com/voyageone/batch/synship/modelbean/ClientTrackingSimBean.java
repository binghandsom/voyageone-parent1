package com.voyageone.batch.synship.modelbean;

/**
 * 获取的需要同步的运单
 *
 * Created by jack on 15/8/14.
 */
public class ClientTrackingSimBean  extends ClientTrackingBean {

    private String syn_ship_no;

    private long order_number;

    private String reservation_id;

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

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }
}
