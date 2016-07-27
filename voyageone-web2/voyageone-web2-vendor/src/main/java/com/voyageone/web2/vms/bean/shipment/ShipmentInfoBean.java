package com.voyageone.web2.vms.bean.shipment;

import java.util.List;

/**
 * shipment的搜索结果
 * Created by vantis on 16-7-25.
 */
public class ShipmentInfoBean {
    private List<ShipmentBean> shipmentList;
    private int total;

    public List<ShipmentBean> getShipmentList() {
        return shipmentList;
    }

    public void setShipmentList(List<ShipmentBean> shipmentList) {
        this.shipmentList = shipmentList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
