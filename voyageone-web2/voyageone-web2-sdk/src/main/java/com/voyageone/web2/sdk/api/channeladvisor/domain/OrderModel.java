package com.voyageone.web2.sdk.api.channeladvisor.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.web2.sdk.api.channeladvisor.enums.OrderStatusEnum;

import java.util.Date;
import java.util.List;

/**
 * @author aooer 2016/9/7.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderModel extends CABaseModel {

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
    private Double rotalFees;

    @JsonProperty("TotalGiftOptionPrice")
    private Double totalGiftOptionPrice;

    @JsonProperty("TotalGiftOptionTaxPrice")
    private Double totalGiftOptionTaxPrice;

    @JsonProperty("TotalPrice")
    private Double totalPrice;

    @JsonProperty("TotalTaxPrice")
    private Double totalTaxPrice;

    @JsonProperty("TotalShippingPrice")
    private Double totalShippingPrice;

    @JsonProperty("TotalShippingTaxPrice")
    private Double totalShippingTaxPrice;

    @JsonProperty("VatInclusive")
    private Boolean vatInclusive;

    @JsonProperty("BuyerAddress")
    private OrderAddressModel buyerAddress;

    @JsonProperty("DeliverByDateUtc")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSXXX", timezone = "UTC")
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

    public Double getRotalFees() {
        return rotalFees;
    }

    public void setRotalFees(Double rotalFees) {
        this.rotalFees = rotalFees;
    }

    public Double getTotalGiftOptionPrice() {
        return totalGiftOptionPrice;
    }

    public void setTotalGiftOptionPrice(Double totalGiftOptionPrice) {
        this.totalGiftOptionPrice = totalGiftOptionPrice;
    }

    public Double getTotalGiftOptionTaxPrice() {
        return totalGiftOptionTaxPrice;
    }

    public void setTotalGiftOptionTaxPrice(Double totalGiftOptionTaxPrice) {
        this.totalGiftOptionTaxPrice = totalGiftOptionTaxPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalTaxPrice() {
        return totalTaxPrice;
    }

    public void setTotalTaxPrice(Double totalTaxPrice) {
        this.totalTaxPrice = totalTaxPrice;
    }

    public Double getTotalShippingPrice() {
        return totalShippingPrice;
    }

    public void setTotalShippingPrice(Double totalShippingPrice) {
        this.totalShippingPrice = totalShippingPrice;
    }

    public Double getTotalShippingTaxPrice() {
        return totalShippingTaxPrice;
    }

    public void setTotalShippingTaxPrice(Double totalShippingTaxPrice) {
        this.totalShippingTaxPrice = totalShippingTaxPrice;
    }

    public Boolean isVatInclusive() {
        return vatInclusive;
    }

    public void setVatInclusive(Boolean vatInclusive) {
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
