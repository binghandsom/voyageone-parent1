package com.voyageone.web2.vms.bean.order;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

/**
 * 订单检索条件
 * Created by vantis on 16-7-7.
 */
public class OrderSearchInfo {
    private Integer status = null;
    private String orderId;
    private String sku;
    private int size = DEFAULT_PAGE_SIZE;
    private int curr = 1;

    @DateTimeFormat
    private Date orderDateFrom = null;

    @DateTimeFormat
    private Date orderDateTo = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (StringUtil.isEmpty(orderId)) this.orderId = null;
        else this.orderId = orderId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        if (StringUtil.isEmpty(sku)) this.sku = null;
        else this.sku = sku;
    }

    public Date getOrderDateFrom() {
        return orderDateFrom;
    }

    public void setOrderDateFrom(long orderDateFrom) {
        this.orderDateFrom = new Date(orderDateFrom);
    }

    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(long orderDateTo) {
        this.orderDateTo = new Date(orderDateTo);
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
