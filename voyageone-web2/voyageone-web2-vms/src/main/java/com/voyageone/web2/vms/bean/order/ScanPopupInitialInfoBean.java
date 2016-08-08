package com.voyageone.web2.vms.bean.order;

import com.voyageone.service.bean.vms.shipment.ShipmentBean;

/**
 * 订单级扫码界面初始化条件
 * Created by vantis on 16-7-19.
 */
public class ScanPopupInitialInfoBean {
    private ShipmentBean shipment;
    private String consolidationOrderId;

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

}
