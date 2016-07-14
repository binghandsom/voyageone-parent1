package com.voyageone.web2.vms.bean.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 平台级别的订单详情
 * Created by vantis on 16-7-6.
 */
public class PlatformSubOrderInfoBean extends AbstractSubOrderInfoBean {
    private String orderId;
    private Date orderDateTime;
    private String status;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private List<SubOrderInfoBean> orderInfoBeanList = new ArrayList<>();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDateTime() {
        return orderDateTime;
    }

    public long getOrderDateTimestamp() {
        return orderDateTime.getTime();
    }

    public void setOrderDateTimestamp(long orderDateTimestamp) {
        this.orderDateTime = new Date(orderDateTimestamp);
    }

    public void setOrderDateTime(Date orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<SubOrderInfoBean> getOrderInfoBeanList() {
        return orderInfoBeanList;
    }

    public void setOrderInfoBeanList(List<SubOrderInfoBean> orderInfoBeanList) {
        this.orderInfoBeanList = orderInfoBeanList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean pushOrderInfoBean(SubOrderInfoBean orderInfoBean) {

        // TODO: 16-7-6 暂未添加对订单的验证 vantis
//        if (null == orderInfoBean || !orderInfoBean.isValid()) {
//
//        }

        if (null == this.status) this.status = orderInfoBean.getStatus();
        else if (!orderInfoBean.getStatus().equals(this.status)) this.status = "-";
        this.totalPrice = totalPrice.add(orderInfoBean.getPrice());
        this.orderInfoBeanList.add(orderInfoBean);
        return true;
    }
}
