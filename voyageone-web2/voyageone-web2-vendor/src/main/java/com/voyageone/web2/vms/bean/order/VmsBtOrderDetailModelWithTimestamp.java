package com.voyageone.web2.vms.bean.order;

import com.voyageone.common.util.BeanUtil;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;

import java.util.Date;

/**
 * 新增对time类型的转换
 * Created by vantis on 16-7-19.
 */
public class VmsBtOrderDetailModelWithTimestamp extends VmsBtOrderDetailModel {

    public static VmsBtOrderDetailModelWithTimestamp getInstance(VmsBtOrderDetailModel vmsBtOrderDetailModel) {
        VmsBtOrderDetailModelWithTimestamp vmsBtOrderDetailModelWithTimestamp = new VmsBtOrderDetailModelWithTimestamp();
        BeanUtil.copy(vmsBtOrderDetailModel, vmsBtOrderDetailModelWithTimestamp);
        return vmsBtOrderDetailModelWithTimestamp;
    }

    public Long getCreatedTimestamp() {
        if (null == super.getCreated()) return null;
        return super.getCreated().getTime();
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        super.setCreated(new Date(createdTimestamp));
    }

    public Long getModifiedTimestamp() {
        if (null == super.getModified()) return null;
        return super.getModified().getTime();
    }

    public void setModifiedTimestamp(long modifiedTimestamp) {
        super.setModified(new Date(modifiedTimestamp));
    }

    public Long getConsolidationOrderTimestamp() {
        if (null == super.getConsolidationOrderTime()) return null;
        return super.getConsolidationOrderTime().getTime();
    }

    public void setConsolidationOrderTimestamp(long consolidationOrderTimestamp) {
        super.setConsolidationOrderTime(new Date(consolidationOrderTimestamp));
    }

    public Long getOrderTimestamp() {
        if (null == super.getOrderTime()) return null;
        return super.getOrderTime().getTime();
    }

    public void setOrderTimestamp(long setOrderTimestamp) {
        super.setOrderTime(new Date(setOrderTimestamp));
    }

    public Long getShipmentTimestamp() {
        if (null == super.getShipmentTime()) return null;
        return super.getShipmentTime().getTime();
    }

    public void setShipmentTimestamp(long shipmentTimestamp) {
        super.setShipmentTime(new Date(shipmentTimestamp));
    }

    public Long getContainerizingTimestamp() {
        if (null == super.getContainerizingTime()) return null;
        return super.getContainerizingTime().getTime();
    }

    public void setContainerizingTimestamp(long containerizingTimestamp) {
        super.setContainerizingTime(new Date(containerizingTimestamp));
    }

    public Long getReceivedTimestamp() {
        if (null == super.getReceivedTime()) return null;
        return super.getReceivedTime().getTime();
    }

    public void setReceivedTimestamp(long receivedTimestamp) {
        super.setReceivedTime(new Date(receivedTimestamp));
    }
}
