package com.voyageone.service.model.wms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * @author jerry 15/12/30
 * @version 2.0.0
 */
public class WmsBtInventoryCenterLogicModel extends BaseModel {

    private int seq;
    private String orderChannelId;
    private String code;
    private String sku;
    private Integer qtyOrgin = 0;
    private Integer qtyChina = 0;
    private boolean active;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQtyOrgin() {
        return qtyOrgin;
    }

    public void setQtyOrgin(Integer qtyOrgin) {
        this.qtyOrgin = qtyOrgin;
    }

    public int getQtyChina() {
        return qtyChina;
    }

    public void setQtyChina(Integer qtyChina) {
        this.qtyChina = qtyChina;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
