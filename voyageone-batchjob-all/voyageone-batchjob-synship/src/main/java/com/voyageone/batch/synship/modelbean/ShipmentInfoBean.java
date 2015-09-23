package com.voyageone.batch.synship.modelbean;

/**
 * tt_shipment
 *
 * Created by jack on 15/8/14.
 */
public class ShipmentInfoBean {

    private String shipment_id;
    private String order_channel_id;
    private String tracking_status;
    private String proccess_type;
    private String proccess_time;
    private String msg;
    private String create_time;
    private String update_time;
    private String create_person;
    private String update_person;

    public String getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(String shipment_id) {
        this.shipment_id = shipment_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getTracking_status() {
        return tracking_status;
    }

    public void setTracking_status(String tracking_status) {
        this.tracking_status = tracking_status;
    }

    public String getProccess_type() {
        return proccess_type;
    }

    public void setProccess_type(String proccess_type) {
        this.proccess_type = proccess_type;
    }

    public String getProccess_time() {
        return proccess_time;
    }

    public void setProccess_time(String proccess_time) {
        this.proccess_time = proccess_time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_person() {
        return create_person;
    }

    public void setCreate_person(String create_person) {
        this.create_person = create_person;
    }

    public String getUpdate_person() {
        return update_person;
    }

    public void setUpdate_person(String update_person) {
        this.update_person = update_person;
    }
}
