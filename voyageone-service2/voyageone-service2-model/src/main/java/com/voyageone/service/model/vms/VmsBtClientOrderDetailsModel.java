package com.voyageone.service.model.vms;

/**
 * @author aooer 2016/9/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VmsBtClientOrderDetailsModel {

    private String orderChannelId;
    private String clientOrderId;
    private Long orderNumber;
    private Long reservationId;
    private String orderItemId;
    private String sellerSku;
    private Double unitPrice;
    private Integer quantity;
    private String status;
    private String shippedDate;
    private String trackingNumber;
    private String shippingCarrier;
    private String shippingClass;
    private String cancelReason;
    private String refundFlg;
    private int active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;


    public String getOrderChannelId() {
        return this.orderChannelId;
    }


    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId;
    }


    public String getClientOrderId() {
        return this.clientOrderId;
    }


    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }


    public Long getOrderNumber() {
        return this.orderNumber;
    }


    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }


    public Long getReservationId() {
        return this.reservationId;
    }


    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }


    public String getOrderItemId() {
        return this.orderItemId;
    }


    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }


    public String getSellerSku() {
        return this.sellerSku;
    }


    public void setSellerSku(String sellerSku) {
        this.sellerSku = sellerSku;
    }


    public Double getUnitPrice() {
        return this.unitPrice;
    }


    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }


    public Integer getQuantity() {
        return this.quantity;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public String getStatus() {
        return this.status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getShippedDate() {
        return this.shippedDate;
    }


    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }


    public String getTrackingNumber() {
        return this.trackingNumber;
    }


    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }


    public String getShippingCarrier() {
        return this.shippingCarrier;
    }


    public void setShippingCarrier(String shippingCarrier) {
        this.shippingCarrier = shippingCarrier;
    }


    public String getShippingClass() {
        return this.shippingClass;
    }


    public void setShippingClass(String shippingClass) {
        this.shippingClass = shippingClass;
    }


    public String getCancelReason() {
        return this.cancelReason;
    }


    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }


    public String getRefundFlg() {
        return this.refundFlg;
    }


    public void setRefundFlg(String refundFlg) {
        this.refundFlg = refundFlg;
    }


    public int getActive() {
        return this.active;
    }


    public void setActive(int active) {
        this.active = active;
    }


    public String getCreated() {
        return this.created;
    }


    public void setCreated(String created) {
        this.created = created;
    }


    public String getCreater() {
        return this.creater;
    }


    public void setCreater(String creater) {
        this.creater = creater;
    }


    public String getModified() {
        return this.modified;
    }


    public void setModified(String modified) {
        this.modified = modified;
    }


    public String getModifier() {
        return this.modifier;
    }


    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
