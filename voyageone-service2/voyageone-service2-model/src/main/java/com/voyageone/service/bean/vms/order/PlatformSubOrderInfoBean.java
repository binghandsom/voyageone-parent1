package com.voyageone.service.bean.vms.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 平台级别的订单详情
 * Created by vantis on 16-7-6.
 */
public class PlatformSubOrderInfoBean extends AbstractSubOrderInfoBean {
    private String consolidationOrderId;
    private Date consolidationOrderTime;
    private String status;
    private BigDecimal totalVoPrice = BigDecimal.ZERO;
    private BigDecimal totalRetailPrice = BigDecimal.ZERO;
    private List<SubOrderInfoBean> orderInfoBeanList = new ArrayList<>();

    public String getConsolidationOrderId() {
        return consolidationOrderId;
    }

    public void setConsolidationOrderId(String consolidationOrderId) {
        this.consolidationOrderId = consolidationOrderId;
    }

    public Date getConsolidationOrderTime() {
        return consolidationOrderTime;
    }

    public void setConsolidationOrderTime(Date consolidationOrderTime) {
        this.consolidationOrderTime = consolidationOrderTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalVoPrice() {
        return totalVoPrice;
    }

    public void setTotalVoPrice(BigDecimal totalVoPrice) {
        this.totalVoPrice = totalVoPrice;
    }

    public BigDecimal getTotalRetailPrice() {
        return totalRetailPrice;
    }

    public void setTotalRetailPrice(BigDecimal totalRetailPrice) {
        this.totalRetailPrice = totalRetailPrice;
    }

    public List<SubOrderInfoBean> getOrderInfoBeanList() {
        return orderInfoBeanList;
    }

    public void setOrderInfoBeanList(List<SubOrderInfoBean> orderInfoBeanList) {
        this.orderInfoBeanList = orderInfoBeanList;
    }

    public boolean pushOrderInfoBean(SubOrderInfoBean orderInfoBean) {

        // TODO: 16-7-6 暂未添加对订单的验证 vantis
//        if (null == orderInfoBean || !orderInfoBean.isValid()) {
//
//        }

        if (null == this.status) this.status = orderInfoBean.getStatus();
        else if (!orderInfoBean.getStatus().equals(this.status)) this.status = "-";
        this.totalVoPrice = totalVoPrice.add(orderInfoBean.getClientPromotionPrice());
        this.totalRetailPrice = totalRetailPrice.add(orderInfoBean.getRetailPrice());
        this.orderInfoBeanList.add(orderInfoBean);
        return true;
    }
}
