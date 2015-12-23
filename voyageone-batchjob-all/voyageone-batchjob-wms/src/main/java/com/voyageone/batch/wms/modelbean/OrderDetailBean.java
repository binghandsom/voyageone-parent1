package com.voyageone.batch.wms.modelbean;

import java.util.List;

/**
 * @author jack
 * 订单Bean
 */
public class OrderDetailBean {

    /**
     * 订单号
     */
    private long order_number;

    /**
     * 品牌方订单号
     */
    private String client_order_id;

    /**
     * Synship物流单号
     */
    private String syn_ship_no;

    /**
     * 外部订单号
     */
    private String source_order_id;

    /**
     * 物品Items
     */
    private List<ReservationBean> lstReservation;


    public long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(long order_number) {
        this.order_number = order_number;
    }

    public String getClient_order_id() {
        return client_order_id;
    }

    public void setClient_order_id(String client_order_id) {
        this.client_order_id = client_order_id;
    }

    public String getSyn_ship_no() {
        return syn_ship_no;
    }

    public void setSyn_ship_no(String syn_ship_no) {
        this.syn_ship_no = syn_ship_no;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public List<ReservationBean> getLstReservation() {
        return lstReservation;
    }

    public void setLstReservation(List<ReservationBean> lstReservation) {
        this.lstReservation = lstReservation;
    }

}
