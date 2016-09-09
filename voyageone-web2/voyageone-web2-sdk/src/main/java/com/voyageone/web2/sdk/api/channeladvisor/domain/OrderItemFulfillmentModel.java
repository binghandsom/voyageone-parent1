package com.voyageone.web2.sdk.api.channeladvisor.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderItemFulfillmentModel extends CABaseModel {

    @JsonProperty("OrderItemID")
    private String orderItemID;

    @JsonProperty("Quantity")
    private Integer quantity;

    @JsonProperty("SellerSku")
    private String sellerSku;

    @JsonProperty("ShippedDateUtc")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSXXX", timezone = "UTC")
    private Date shippedDateUtc;

    @JsonProperty("ShippingCarrier")
    private String shippingCarrier;

    @JsonProperty("ShippingClass")
    private String shippingClass;

    @JsonProperty("TrackingNumber")
    private String trackingNumber;

    public String getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(String orderItemID) {
        this.orderItemID = orderItemID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSellerSku() {
        return sellerSku;
    }

    public void setSellerSku(String sellerSku) {
        this.sellerSku = sellerSku;
    }

    public Date getShippedDateUtc() {
        return shippedDateUtc;
    }

    public void setShippedDateUtc(Date shippedDateUtc) {
        this.shippedDateUtc = shippedDateUtc;
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

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

}
