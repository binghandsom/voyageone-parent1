package com.voyageone.web2.vms.bean.order;

import org.apache.commons.net.ntp.TimeStamp;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

/**
 * 订单检索条件
 * Created by vantis on 16-7-7.
 */
public class OrderSearchInfo {
    private int status;
    private String orderId;
    private String sku;
    private int size = DEFAULT_PAGE_SIZE;
    private int curr = 1;
    private TimeStamp orderDateFrom;
    private TimeStamp orderDateTo;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public TimeStamp getOrderDateFrom() {
        return orderDateFrom;
    }

    public void setOrderDateFrom(TimeStamp orderDateFrom) {
        this.orderDateFrom = orderDateFrom;
    }

    public TimeStamp getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(TimeStamp orderDateTo) {
        this.orderDateTo = orderDateTo;
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
}
