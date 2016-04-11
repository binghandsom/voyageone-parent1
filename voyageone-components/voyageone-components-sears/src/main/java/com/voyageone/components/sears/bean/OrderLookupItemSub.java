package com.voyageone.components.sears.bean;

/**
 * Created by jerry on 2015/12/2
 */
public class OrderLookupItemSub {

    private String trackingNumber;
    private boolean isAllocated = false;

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public boolean isAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(boolean isAllocated) {
        this.isAllocated = isAllocated;
    }
}
