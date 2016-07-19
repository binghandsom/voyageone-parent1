package com.voyageone.web2.vms.bean.order;

import com.voyageone.web2.vms.bean.shipment.ShipmentBean;

/**
 * 订单级扫码界面初始化条件
 * Created by vantis on 16-7-19.
 */
public class ScanPopupInitialInfo {
    private ShipmentBean shipmentBean;
    private String orderId;

    public ShipmentBean getShipmentBean() {
        return shipmentBean;
    }

    public void setShipmentBean(ShipmentBean shipmentBean) {
        this.shipmentBean = shipmentBean;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
