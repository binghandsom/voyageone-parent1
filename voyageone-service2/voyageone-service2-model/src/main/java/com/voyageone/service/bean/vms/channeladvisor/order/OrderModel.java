package com.voyageone.service.bean.vms.channeladvisor.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.bean.vms.channeladvisor.CABaseModel;
import com.voyageone.service.bean.vms.channeladvisor.enums.OrderStatusEnum;

import java.math.BigDecimal;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", timezone = "UTC")
    private Date orderDateUtc;

    @JsonProperty("OrderStatus")
    private OrderStatusEnum orderStatus;

    @JsonProperty("RequestedShippingMethod")
    private String requestedShippingMethod;

    @JsonProperty("TotalFees")
    private BigDecimal totalFees;

    @JsonProperty("TotalGiftOptionPrice")
    private BigDecimal totalGiftOptionPrice;

    @JsonProperty("TotalGiftOptionTaxPrice")
    private BigDecimal totalGiftOptionTaxPrice;

    @JsonProperty("TotalPrice")
    private BigDecimal totalPrice;

    @JsonProperty("TotalTaxPrice")
    private BigDecimal totalTaxPrice;

    @JsonProperty("TotalShippingPrice")
    private BigDecimal totalShippingPrice;

    @JsonProperty("TotalShippingTaxPrice")
    private BigDecimal totalShippingTaxPrice;

    @JsonProperty("VatInclusive")
    private Boolean vatInclusive;

    @JsonProperty("BuyerAddress")
    private OrderAddressModel buyerAddress;

    @JsonProperty("DeliverByDateUtc")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", timezone = "UTC")
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

    public BigDecimal getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(BigDecimal rotalFees) {
        this.totalFees = rotalFees;
    }

    public BigDecimal getTotalGiftOptionPrice() {
        return totalGiftOptionPrice;
    }

    public void setTotalGiftOptionPrice(BigDecimal totalGiftOptionPrice) {
        this.totalGiftOptionPrice = totalGiftOptionPrice;
    }

    public BigDecimal getTotalGiftOptionTaxPrice() {
        return totalGiftOptionTaxPrice;
    }

    public void setTotalGiftOptionTaxPrice(BigDecimal totalGiftOptionTaxPrice) {
        this.totalGiftOptionTaxPrice = totalGiftOptionTaxPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalTaxPrice() {
        return totalTaxPrice;
    }

    public void setTotalTaxPrice(BigDecimal totalTaxPrice) {
        this.totalTaxPrice = totalTaxPrice;
    }

    public BigDecimal getTotalShippingPrice() {
        return totalShippingPrice;
    }

    public void setTotalShippingPrice(BigDecimal totalShippingPrice) {
        this.totalShippingPrice = totalShippingPrice;
    }

    public BigDecimal getTotalShippingTaxPrice() {
        return totalShippingTaxPrice;
    }

    public void setTotalShippingTaxPrice(BigDecimal totalShippingTaxPrice) {
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
