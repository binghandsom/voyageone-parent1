package com.voyageone.web2.vms.bean.shipment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.BeanUtil;
import com.voyageone.service.model.vms.VmsBtShipmentModel;

import java.util.Date;

/**
 * shipment增加对timestamp的方法
 * Created by vantis on 2016/7/15.
 */
public class ShipmentBean {
    private Integer Id;
    private String channelId;
    private String shipmentName;
    private Date shippedDate;
    private String expressCompany;
    private String trackingNo;
    private String comment;
    private int orderTotal;
    private int skuTotal;

    /**
     * 1:Open；3：Shipped；4：Arrived；5：Received；6：Receive with Error
     */
    private String status;

    /**
     * 到达VoyageOne时间
     */
    private Date arrivedTime;

    /**
     * 到达确认者
     */
    private String arriver;

    /**
     * VoyageOne确认收货时间
     */
    private Date receivedTime;

    /**
     * 确认收货者
     */
    private String receiver;

    public static ShipmentBean getInstance(VmsBtShipmentModel vmsBtShipmentModel) {
        ShipmentBean shipmentBean = new ShipmentBean();
        BeanUtil.copy(vmsBtShipmentModel, shipmentBean);
        return shipmentBean;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

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

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(Date arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public String getArriver() {
        return arriver;
    }

    public void setArriver(String arriver) {
        this.arriver = arriver;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getSkuTotal() {
        return skuTotal;
    }

    public void setSkuTotal(int skuTotal) {
        this.skuTotal = skuTotal;
    }
}
