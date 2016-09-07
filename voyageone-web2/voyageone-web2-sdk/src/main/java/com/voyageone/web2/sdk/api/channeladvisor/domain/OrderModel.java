package com.voyageone.web2.sdk.api.channeladvisor.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.web2.sdk.api.channeladvisor.enums.OrderStatusEnum;

import java.util.Date;
import java.util.List;

/**
 * @author aooer 2016/9/7.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderModel {

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Items")
    private List<OrderItemModel> items;

    @JsonProperty("OrderDateUtc")
    private Date orderDateUtc;

    @JsonProperty("OrderStatus")
    private OrderStatusEnum orderStatus;

    @JsonProperty("RequestedShippingMethod")
    private String requestedShippingMethod;

    @JsonProperty("TotalFees")
    private double rotalFees;

    @JsonProperty("TotalGiftOptionPrice")
    private double totalGiftOptionPrice;

    @JsonProperty("TotalGiftOptionTaxPrice")
    private double totalGiftOptionTaxPrice;

    @JsonProperty("TotalPrice")
    private double totalPrice;

    @JsonProperty("TotalTaxPrice")
    private double totalTaxPrice;

    @JsonProperty("TotalShippingPrice")
    private double totalShippingPrice;

    @JsonProperty("TotalShippingTaxPrice")
    private double totalShippingTaxPrice;

    @JsonProperty("VatInclusive")
    private boolean vatInclusive;

    @JsonProperty("BuyerAddress")
    private OrderAddressModel buyerAddress;

    @JsonProperty("DeliverByDateUtc")
    private Date deliverByDateUtc;

    @JsonProperty("ShippingAddress")
    private OrderAddressModel shippingAddress;

    @JsonProperty("ShippingLabelURL")
    private String shippingLabelURL;


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OrderItemModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemModel> items) {
        this.items = items;
    }

    public Date getOrderDateUtc() {
        return orderDateUtc;
    }

    public void setOrderDateUtc(Date orderDateUtc) {
        this.orderDateUtc = orderDateUtc;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRequestedShippingMethod() {
        return requestedShippingMethod;
    }

    public void setRequestedShippingMethod(String requestedShippingMethod) {
        this.requestedShippingMethod = requestedShippingMethod;
    }

    public double getRotalFees() {
        return rotalFees;
    }

    public void setRotalFees(double rotalFees) {
        this.rotalFees = rotalFees;
    }

    public double getTotalGiftOptionPrice() {
        return totalGiftOptionPrice;
    }

    public void setTotalGiftOptionPrice(double totalGiftOptionPrice) {
        this.totalGiftOptionPrice = totalGiftOptionPrice;
    }

    public double getTotalGiftOptionTaxPrice() {
        return totalGiftOptionTaxPrice;
    }

    public void setTotalGiftOptionTaxPrice(double totalGiftOptionTaxPrice) {
        this.totalGiftOptionTaxPrice = totalGiftOptionTaxPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalTaxPrice() {
        return totalTaxPrice;
    }

    public void setTotalTaxPrice(double totalTaxPrice) {
        this.totalTaxPrice = totalTaxPrice;
    }

    public double getTotalShippingPrice() {
        return totalShippingPrice;
    }

    public void setTotalShippingPrice(double totalShippingPrice) {
        this.totalShippingPrice = totalShippingPrice;
    }

    public double getTotalShippingTaxPrice() {
        return totalShippingTaxPrice;
    }

    public void setTotalShippingTaxPrice(double totalShippingTaxPrice) {
        this.totalShippingTaxPrice = totalShippingTaxPrice;
    }

    public boolean isVatInclusive() {
        return vatInclusive;
    }

    public void setVatInclusive(boolean vatInclusive) {
        this.vatInclusive = vatInclusive;
    }

    public OrderAddressModel getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(OrderAddressModel buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public Date getDeliverByDateUtc() {
        return deliverByDateUtc;
    }

    public void setDeliverByDateUtc(Date deliverByDateUtc) {
        this.deliverByDateUtc = deliverByDateUtc;
    }

    public OrderAddressModel getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(OrderAddressModel shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingLabelURL() {
        return shippingLabelURL;
    }

    public void setShippingLabelURL(String shippingLabelURL) {
        this.shippingLabelURL = shippingLabelURL;
    }
}
