package com.voyageone.web2.vms.bean.shipment;

/**
 * shipment关闭发货的统计内容
 * Created by vantis on 16-7-21.
 */
public class ShipmentEndCountBean {
    private int canceledSkuCount;
    private int succeedSkuCount;
    private int succeedShipmentCount;

    public int getCanceledSkuCount() {
        return canceledSkuCount;
    }

    public void setCanceledSkuCount(int canceledSkuCount) {
        this.canceledSkuCount = canceledSkuCount;
    }

    public int getSucceedSkuCount() {
        return succeedSkuCount;
    }

    public void setSucceedSkuCount(int succeedSkuCount) {
        this.succeedSkuCount = succeedSkuCount;
    }

    public int getSucceedShipmentCount() {
        return succeedShipmentCount;
    }

    public void setSucceedShipmentCount(int succeedShipmentCount) {
        this.succeedShipmentCount = succeedShipmentCount;
    }
}
