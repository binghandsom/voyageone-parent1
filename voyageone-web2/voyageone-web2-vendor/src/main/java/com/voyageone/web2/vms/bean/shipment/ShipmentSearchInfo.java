package com.voyageone.web2.vms.bean.shipment;

import com.voyageone.common.util.StringUtils;

import java.util.Date;

import static com.voyageone.web2.vms.VmsConstants.DEFAULT_PAGE_SIZE;

/**
 * shipmen搜索条件
 * Created by vantis on 16-7-25.
 */
public class ShipmentSearchInfo {
    private String status;
    private String shipmentName;
    private String trackingNo;
    private int size = DEFAULT_PAGE_SIZE;
    private int curr = 1;
    private Date shippedDateFrom = null;
    private Date shippedDateTo = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        if (!StringUtils.isEmpty(shipmentName))
            this.shipmentName = shipmentName;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        if (!StringUtils.isEmpty(trackingNo))
            this.trackingNo = trackingNo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public Date getShippedDateFrom() {
        return shippedDateFrom;
    }

    public void setShippedDateFrom(Date shippedDateFrom) {
        this.shippedDateFrom = shippedDateFrom;
    }

    public Date getShippedDateTo() {
        return shippedDateTo;
    }

    public void setShippedDateTo(Date shippedDateTo) {
        this.shippedDateTo = shippedDateTo;
    }
}
