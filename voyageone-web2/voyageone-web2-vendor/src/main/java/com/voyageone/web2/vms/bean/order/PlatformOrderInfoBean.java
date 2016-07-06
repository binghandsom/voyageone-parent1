package com.voyageone.web2.vms.bean.order;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台级别的订单详情
 * Created by vantis on 16-7-6.
 */
public class PlatformOrderInfoBean {
    private String orderId;
    private long orderDateTimestamp;
    private double totalPrice = 0;
    List<OrderInfoBean> orderInfoBeanList = new ArrayList<>();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderDateTimestamp() {
        return orderDateTimestamp;
    }

    public void setOrderDateTimestamp(long orderDateTimestamp) {
        this.orderDateTimestamp = orderDateTimestamp;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderInfoBean> getOrderInfoBeanList() {
        return orderInfoBeanList;
    }

    public void setOrderInfoBeanList(List<OrderInfoBean> orderInfoBeanList) {
        this.orderInfoBeanList = orderInfoBeanList;
    }

    public boolean pushOrderInfoBean(OrderInfoBean orderInfoBean) {

        // TODO: 16-7-6 需要添加对订单的验证、价格的计算 vantis
//        if (null == orderInfoBean || orderInfoBean.isNotValid) {
//
//        }
        return orderInfoBeanList.add(orderInfoBean);
    }
}
