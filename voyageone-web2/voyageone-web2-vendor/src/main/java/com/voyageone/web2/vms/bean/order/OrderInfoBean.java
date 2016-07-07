package com.voyageone.web2.vms.bean.order;

import java.math.BigDecimal;

/**
 * 单条的订单信息
 * Created by vantis on 16-7-6.
 */
public class OrderInfoBean extends AbstractOrderInfoBean {
    private String orderId;
    private long orderDateTimestamp;
    private String sku;
    private String desc;
    private String status;
    private BigDecimal price;

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // TODO: 16-7-7 暂未想好后续处理 未做订单信息验证 vantis
    public boolean isValid() {
        return true;
    }
}
