package com.voyageone.web2.vms.bean.order;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 单条的订单信息
 * Created by vantis on 16-7-6.
 */
public class SubOrderInfoBean extends AbstractSubOrderInfoBean {
    private String reservationId;
    private String consolidationOrderId;
    private Date consolidationOrderTime;
    private String sku;
    private String name;
    private String status;
    private BigDecimal voPrice = BigDecimal.ZERO;
    private BigDecimal retailPrice = BigDecimal.ZERO;

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getConsolidationOrderId() {
        return consolidationOrderId;
    }

    public void setConsolidationOrderId(String consolidationOrderId) {
        this.consolidationOrderId = consolidationOrderId;
    }

    public long getconsolidationOrderTimestamp() {
        return consolidationOrderTime.getTime();
    }

    public void setconsolidationOrderTimestamp(long consolidationOrderTimestamp) {
        this.consolidationOrderTime = new Date(consolidationOrderTimestamp);
    }

    public void setConsolidationOrderTime(Date consolidationOrderTime) {
        this.consolidationOrderTime = consolidationOrderTime;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getVoPrice() {
        return voPrice;
    }

    public void setVoPrice(BigDecimal voPrice) {
        this.voPrice = voPrice;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    // TODO: 16-7-7 暂未想好后续处理 未做订单信息验证 vantis
    public boolean isValid() {
        return true;
    }
}
