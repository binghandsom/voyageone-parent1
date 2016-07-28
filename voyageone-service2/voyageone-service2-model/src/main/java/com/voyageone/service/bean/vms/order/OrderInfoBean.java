package com.voyageone.service.bean.vms.order;

import java.util.List;

/**
 * 订单信息:列表和总数
 * Created by vantis on 16-7-8.
 */
public class OrderInfoBean {
    private long total;
    private List<AbstractSubOrderInfoBean> orderList;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<AbstractSubOrderInfoBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<AbstractSubOrderInfoBean> orderList) {
        this.orderList = orderList;
    }
}
