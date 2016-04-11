package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by james.li on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name ="order")
public class OrderLookupResponse {

    private String orderId;
    private String omsOrderId;
    private String customerReference;
    private String orderReference;
    private String orderDate;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<OrderLookupItem> items;
    private OrderShippingBean shippingAddress;

    private float subTotal;
    private float shipping;
    private float shipTax;
    private float tax;
    private float shippingAdjustment;
    private float grandTotal;

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOmsOrderId() {
        return omsOrderId;
    }

    public void setOmsOrderId(String omsOrderId) {
        this.omsOrderId = omsOrderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public float getShipTax() {
        return shipTax;
    }

    public void setShipTax(float shipTax) {
        this.shipTax = shipTax;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getShippingAdjustment() {
        return shippingAdjustment;
    }

    public void setShippingAdjustment(float shippingAdjustment) {
        this.shippingAdjustment = shippingAdjustment;
    }

    public float getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(float grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<OrderLookupItem> getItems() {
        return items;
    }

    public void setItems(List<OrderLookupItem> items) {
        this.items = items;
    }

    public OrderShippingBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(OrderShippingBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
