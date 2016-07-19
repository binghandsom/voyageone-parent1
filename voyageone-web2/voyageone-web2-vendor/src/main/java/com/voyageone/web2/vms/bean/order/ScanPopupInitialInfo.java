package com.voyageone.web2.vms.bean.order;

import com.voyageone.web2.vms.bean.shipment.ShipmentBean;

/**
 * 订单级扫码界面初始化条件
 * Created by vantis on 16-7-19.
 */
public class ScanPopupInitialInfo {
    private ShipmentBean shipment;
    private String orderId;
    private int curr;
    private int size;

    public ShipmentBean getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentBean shipment) {
        this.shipment = shipment;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
