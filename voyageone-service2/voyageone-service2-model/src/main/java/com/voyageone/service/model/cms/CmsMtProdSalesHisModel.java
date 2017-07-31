package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

/**
 * Created by dell on 2016/5/24.
 */
public class CmsMtProdSalesHisModel extends BaseMongoModel {

    private int cart_id;
    private String channel_id;
    private String sku;
    private String date;
    private int qty;
    private String prodCode;
    private String modifier;
    private String modified;
    private String creater;
    private String created;

    @Override
    public String getCreater() {
        return creater;
    }

    @Override
    public void setCreater(String creater) {
        this.creater = creater;
    }

    @Override
    public String getCreated() {
        return created;
    }

    @Override
    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public String getModified() {
        return modified;
    }

    @Override
    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
