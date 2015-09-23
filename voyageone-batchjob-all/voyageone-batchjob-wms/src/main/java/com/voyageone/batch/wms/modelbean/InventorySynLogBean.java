package com.voyageone.batch.wms.modelbean;

/**
 * 通过关联 ims_bt_log_syn_inventory, wms_bt_item_details, ims_bt_product 三表。获取的需要同步的库存记录
 *
 * Created by jonas on 15/6/2.
 */
public class InventorySynLogBean {
    private long seq;
    private String order_channel_id;
    private int cart_id;
    private String sku;
    private int qty;
    private String syn_flg;
    private String syn_type;
    private String modified;
    private String modifier;
    private String barcode;
    private String itemcode;
    private String num_iid;
    private String created;
    private String creater;
    private String remarks;
    private String quantity_update_type;

    public String getSyn_type() {
        return syn_type;
    }

    public void setSyn_type(String syn_type) {
        this.syn_type = syn_type;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
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

    public String getSyn_flg() {
        return syn_flg;
    }

    public void setSyn_flg(String syn_flg) {
        this.syn_flg = syn_flg;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getQuantity_update_type() {
        return quantity_update_type;
    }

    public void setQuantity_update_type(String quantity_update_type) {
        this.quantity_update_type = quantity_update_type;
    }
}
