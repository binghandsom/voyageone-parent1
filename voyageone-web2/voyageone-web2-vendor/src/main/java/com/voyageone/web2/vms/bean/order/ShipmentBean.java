package com.voyageone.web2.vms.bean.order;

/**
 * =。=
 * Created by vantis on 16-7-6.
 */
public class ShipmentBean {
    /**
     * 渠道id
     */
    protected String channelId;

    /**
     * 发货名称
     */
    protected String shipmentName;

    protected String shippedDate;

    /**
     * 物流公司id
     */
    protected String expresscompany;

    /**
     * 物流订单号
     */
    protected String trackingNo;

    /**
     * 注释
     */
    protected String comment;

    /**
     * 到达VoyageOne时间
     */
    protected String arrivedTime;

    /**
     * 到达确认者
     */
    protected String arriver;

    /**
     * VoyageOne确认收货时间
     */
    protected String receivedTime;

    /**
     * 确认收货者
     */
    protected String receiver;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getExpresscompany() {
        return expresscompany;
    }

    public void setExpresscompany(String expresscompany) {
        this.expresscompany = expresscompany;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public String getArriver() {
        return arriver;
    }

    public void setArriver(String arriver) {
        this.arriver = arriver;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
