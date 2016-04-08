package com.voyageone.components.sears.bean;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by james.li on 2015/11/23.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name ="order")
public class OrderBean {

    private String customerReference;
    private String orderReference;
    private String orderTimestamp;
    private String transactionId;
    private double exchangeRate;
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<OrderItem> items;
    private OrderShippingBean shippingAddress;

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

    public String getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(String orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderShippingBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(OrderShippingBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
