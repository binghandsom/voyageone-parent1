package com.voyageone.batch.wms.modelbean;

public class OrderUpdateBean {

    private String created;
    private String creater;
    private String source_order_id;
    private String trackingNo;
    private String client_order_id;
    private String orderStatus;
    private String upc;

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getClient_order_id() {
        return client_order_id;
    }

    public void setClient_order_id(String client_order_id) {
        this.client_order_id = client_order_id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

}
