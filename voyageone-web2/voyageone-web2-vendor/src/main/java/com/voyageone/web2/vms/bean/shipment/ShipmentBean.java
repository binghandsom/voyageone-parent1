package com.voyageone.web2.vms.bean.shipment;

import com.voyageone.common.util.BeanUtil;
import com.voyageone.service.model.vms.VmsBtShipmentModel;

import java.util.Date;

/**
 * shipment增加对timestamp的方法
 * Created by vantis on 2016/7/15.
 */
public class ShipmentBean extends VmsBtShipmentModel {

    public ShipmentBean() {

    }

    public static ShipmentBean getInstance(VmsBtShipmentModel vmsBtShipmentModel) {
        ShipmentBean shipmentBean = new ShipmentBean();
        BeanUtil.copy(vmsBtShipmentModel, shipmentBean);
        return shipmentBean;
    }

    public Long getShippedDateTimestamp() {
        if (null == super.getShippedDate()) return null;
        return super.getShippedDate().getTime();
    }

    public void setShippedDateTimestamp(Long shippedDateTimestamp) {
        if (null != shippedDateTimestamp)
            super.setShippedDate(new Date(shippedDateTimestamp));
    }

    public Long getArrivedTimestamp() {
        if (null == super.getArrivedTime()) return null;
        return super.getArrivedTime().getTime();
    }

    public void setArrivedTimestamp(Long arrivedTimestamp) {
        if (null != arrivedTimestamp)
            super.setArrivedTime(new Date(arrivedTimestamp));
    }

    public Long getReceivedTimestamp() {
        if (null == super.getArrivedTime()) return null;
        return super.getReceivedTime().getTime();
    }

    public void setReceivedTimestamp(Long receivedTimestamp) {
        if (null != receivedTimestamp)
            super.setArrivedTime(new Date(receivedTimestamp));
    }
}
