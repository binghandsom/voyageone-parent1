
package com.voyageone.service.model.vms;

/**
 * @author aooer 2016/9/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VmsBtClientInventoryModel {

    private Long seq;
    private String orderChannelId;
    private String sellerSku;
    private Integer qty;
    private String status;
    private int active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;


    public Long getSeq() {
        return this.seq;
    }


    public void setSeq(Long seq) {
        this.seq = seq;
    }


    public String getOrderChannelId() {
        return this.orderChannelId;
    }


    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId;
    }


    public String getSellerSku() {
        return this.sellerSku;
    }


    public void setSellerSku(String sellerSku) {
        this.sellerSku = sellerSku;
    }


    public Integer getQty() {
        return this.qty;
    }


    public void setQty(Integer qty) {
        this.qty = qty;
    }


    public String getStatus() {
        return this.status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public int getActive() {
        return this.active;
    }


    public void setActive(int active) {
        this.active = active;
    }


    public String getCreated() {
        return this.created;
    }


    public void setCreated(String created) {
        this.created = created;
    }


    public String getCreater() {
        return this.creater;
    }


    public void setCreater(String creater) {
        this.creater = creater;
    }


    public String getModified() {
        return this.modified;
    }


    public void setModified(String modified) {
        this.modified = modified;
    }


    public String getModifier() {
        return this.modifier;
    }


    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
