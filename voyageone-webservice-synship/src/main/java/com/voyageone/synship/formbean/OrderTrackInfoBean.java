package com.voyageone.synship.formbean;

import com.voyageone.synship.modelbean.TrackingInfoConfigBean;

/**
 * Created by dell on 2015/7/23.
 */
public class OrderTrackInfoBean extends TrackingInfoConfigBean {

    // 物流订单号
    private String syn_ship_no;
    // 订单渠道
    private String order_channel_id;
    // card_id
    private String card_id;

    public String getSyn_ship_no() {
        return syn_ship_no;
    }

    public void setSyn_ship_no(String syn_ship_no) {
        this.syn_ship_no = syn_ship_no;
    }

    @Override
    public String getOrder_channel_id() {
        return order_channel_id;
    }

    @Override
    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
}
