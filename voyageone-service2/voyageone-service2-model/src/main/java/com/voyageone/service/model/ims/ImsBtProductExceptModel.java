package com.voyageone.service.model.ims;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by tom.zhu on 16/10/20.
 */
public class ImsBtProductExceptModel extends BaseModel {
    private int seq;
    private String orderChannelId;
    private int cartId;
    private String exceptSku;
    private String comment;
    private int active;

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

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getExceptSku() {
        return exceptSku;
    }

    public void setExceptSku(String exceptSku) {
        this.exceptSku = exceptSku;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
