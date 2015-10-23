package com.voyageone.batch.wms.modelbean;

import java.util.List;

/**
 * @author jack
 * bulkShipmentBean
 */
public class bulkShipmentBean {

    private bulkShipmentHeadBean bulkShipmentHead;
    private List<bulkShipmentDetailBean> bulkShipmentDetails;

    public bulkShipmentHeadBean getBulkShipmentHead() {
        return bulkShipmentHead;
    }

    public void setBulkShipmentHead(bulkShipmentHeadBean bulkShipmentHead) {
        this.bulkShipmentHead = bulkShipmentHead;
    }

    public List<bulkShipmentDetailBean> getBulkShipmentDetails() {
        return bulkShipmentDetails;
    }

    public void setBulkShipmentDetails(List<bulkShipmentDetailBean> bulkShipmentDetails) {
        this.bulkShipmentDetails = bulkShipmentDetails;
    }
}
