
package com.voyageone.web2.sdk.api.channeladvisor.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ShipRequest {

    @JsonProperty("ShippedDateUtc")
    private String shippedDateUtc;

    @JsonProperty("TrackingNumber")
    private String trackingNumber;

    @JsonProperty("ShippingCarrier")
    private String shippingCarrier;

    @JsonProperty("ShippingClass")
    private String shippingClass;

    @JsonProperty("Items")
    private Map<String,Integer> items;

    public String getShippedDateUtc() {
        return shippedDateUtc;
    }

    public void setShippedDateUtc(String shippedDateUtc) {
        this.shippedDateUtc = shippedDateUtc;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getShippingCarrier() {
        return shippingCarrier;
    }

    public void setShippingCarrier(String shippingCarrier) {
        this.shippingCarrier = shippingCarrier;
    }

    public String getShippingClass() {
        return shippingClass;
    }

    public void setShippingClass(String shippingClass) {
        this.shippingClass = shippingClass;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }
}

