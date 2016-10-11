package com.voyageone.web2.vms.bean.order;

/**
 * 下载拣货单的配置
 * Created by vantis on 16-7-12.
 */
public class DownloadInfoBean {
    private String orderType;
    private OrderSearchInfoBean orderSearchInfoBean;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public OrderSearchInfoBean getOrderSearchInfoBean() {
        return orderSearchInfoBean;
    }

    public void setOrderSearchInfoBean(OrderSearchInfoBean orderSearchInfoBean) {
        this.orderSearchInfoBean = orderSearchInfoBean;
    }
}
