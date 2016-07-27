package com.voyageone.web2.vms.bean.order;

import com.voyageone.web2.vms.bean.shipment.ShipmentBean;

/**
 * 扫码前台参数
 * Created by vantis on 16-7-19.
 */
public class ScanInfo {
    private ShipmentBean shipment;
    private String consolidationOrderId;
    private String barcode;

    public ShipmentBean getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentBean shipment) {
        this.shipment = shipment;
    }

    public String getConsolidationOrderId() {
        return consolidationOrderId;
    }

    public void setConsolidationOrderId(String consolidationOrderId) {
        this.consolidationOrderId = consolidationOrderId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
