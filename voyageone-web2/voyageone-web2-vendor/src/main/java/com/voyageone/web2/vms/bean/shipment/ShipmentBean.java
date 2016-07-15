package com.voyageone.web2.vms.bean.shipment;

import com.voyageone.service.model.vms.VmsBtShipmentModel;

import java.util.Date;

/**
 * shipment增加对timestamp的方法
 * Created by vantis on 2016/7/15.
 */
public class ShipmentBean extends VmsBtShipmentModel {

    public ShipmentBean(VmsBtShipmentModel vmsBtShipmentModel) {
        if (null != vmsBtShipmentModel) {
            this.setId(vmsBtShipmentModel.getId());
            this.setShipmentName(vmsBtShipmentModel.getShipmentName());
            this.setShippedDate(vmsBtShipmentModel.getShippedDate());
            this.setExpressCompany(vmsBtShipmentModel.getExpressCompany());
            this.setTrackingNo(vmsBtShipmentModel.getTrackingNo());
            this.setComment(vmsBtShipmentModel.getComment());
            this.setStatus(vmsBtShipmentModel.getStatus());
            this.setArrivedTime(vmsBtShipmentModel.getArrivedTime());
            this.setArriver(vmsBtShipmentModel.getArriver());
            this.setReceivedTime(vmsBtShipmentModel.getReceivedTime());
            this.setReceiver(vmsBtShipmentModel.getReceiver());
            this.setCreated(vmsBtShipmentModel.getCreated());
            this.setCreater(vmsBtShipmentModel.getCreater());
            this.setModified(vmsBtShipmentModel.getModified());
            this.setModifier(vmsBtShipmentModel.getModifier());
        }
    }

    public Long getShippedDateTimestamp() {
        if (null == super.getShippedDate()) return null;
        return super.getShippedDate().getTime();
    }

    public void setShippedDateTimestamp(long shippedDateTimestamp) {
        super.setShippedDate(new Date(shippedDateTimestamp));
    }

    public Long getArrivedTimestamp() {
        if (null == super.getArrivedTime()) return null;
        return super.getArrivedTime().getTime();
    }

    public void setArrivedTimestamp(long arrivedTimestamp) {
        super.setArrivedTime(new Date(arrivedTimestamp));
    }

    public Long getReceivedTimestamp() {
        if (null == super.getArrivedTime()) return null;
        return super.getReceivedTime().getTime();
    }

    public void setReceivedTimestamp(long receivedTimestamp) {
        super.setArrivedTime(new Date(receivedTimestamp));
    }
}
