package com.voyageone.batch.synship.modelbean;

/**
 * tt_client_tracking
 *
 * Created by jack on 15/8/14.
 */
public class ClientTrackingBean {

    private long seq;
    private String order_channel_id;
    private String client_order_id;
    private String source_order_id;
    private String tracking_type;
    private String tracking_no;
    private String tracking_time;
    private String sim_flg;
    private String created;
    private String modifier;
    private String modified;
    private String creater;

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getClient_order_id() {
        return client_order_id;
    }

    public void setClient_order_id(String client_order_id) {
        this.client_order_id = client_order_id;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
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

    public String getTracking_time() {
        return tracking_time;
    }

    public void setTracking_time(String tracking_time) {
        this.tracking_time = tracking_time;
    }

    public String getSim_flg() {
        return sim_flg;
    }

    public void setSim_flg(String sim_flg) {
        this.sim_flg = sim_flg;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
}
