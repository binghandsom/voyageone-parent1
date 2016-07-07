package com.voyageone.web2.vms.bean.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 平台级别的订单详情
 * Created by vantis on 16-7-6.
 */
public class PlatformOrderInfoBean extends AbstractOrderInfoBean {
    private String orderId;
    private long orderDateTimestamp;
    private String status;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private List<OrderInfoBean> orderInfoBeanList = new ArrayList<>();

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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderInfoBean> getOrderInfoBeanList() {
        return orderInfoBeanList;
    }

    public void setOrderInfoBeanList(List<OrderInfoBean> orderInfoBeanList) {
        this.orderInfoBeanList = orderInfoBeanList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean pushOrderInfoBean(OrderInfoBean orderInfoBean) {

        // TODO: 16-7-6 暂未添加对订单的验证 vantis
//        if (null == orderInfoBean || !orderInfoBean.isValid()) {
//
//        }
        this.totalPrice = totalPrice.add(orderInfoBean.getPrice());
        this.orderInfoBeanList.add(orderInfoBean);
        return true;
    }
}
