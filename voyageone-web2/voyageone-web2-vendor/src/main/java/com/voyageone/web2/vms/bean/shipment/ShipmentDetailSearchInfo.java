package com.voyageone.web2.vms.bean.shipment;

/**
 * shipmentDetail的搜索条件
 * Created by vantis on 16-7-26.
 */
public class ShipmentDetailSearchInfo {
    private Integer shipmentId;
    private int curr;
    private int size;
    private int total;

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
