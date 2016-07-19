package com.voyageone.web2.vms.bean.order;

import com.voyageone.web2.vms.bean.shipment.ShipmentBean;

/**
 * 扫码前台参数
 * Created by vantis on 16-7-19.
 */
public class ScanPopupCheckBarcodeInfo {
    private ShipmentBean shipment;
    private String orderId;
    private String barcode;

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
