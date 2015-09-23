package com.voyageone.wms.modelbean;

public class TransferBean {
    private int transfer_id;
    private String order_channel_id;
    private int store_id;
    private String transfer_status;
    private String transfer_type;
    private String transfer_name;
    private String po_number;
    private int transfer_from_store;
    private int transfer_to_store;
    private String transfer_origin;
    private int origin_id;
    private String sim_flg,comment;
    private boolean active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getTransfer_status() {
        return transfer_status;
    }

    public void setTransfer_status(String transfer_status) {
        this.transfer_status = transfer_status;
    }

    public String getTransfer_type() {
        return transfer_type;
    }

    public void setTransfer_type(String transfer_type) {
        this.transfer_type = transfer_type;
    }

    public String getTransfer_name() {
        return transfer_name;
    }

    public void setTransfer_name(String transfer_name) {
        this.transfer_name = transfer_name;
    }

    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public int getTransfer_from_store() {
        return transfer_from_store;
    }

    public void setTransfer_from_store(int transfer_from_store) {
        this.transfer_from_store = transfer_from_store;
    }

    public int getTransfer_to_store() {
        return transfer_to_store;
    }

    public void setTransfer_to_store(int transfer_to_store) {
        this.transfer_to_store = transfer_to_store;
    }

    public String getTransfer_origin() {
        return transfer_origin;
    }

    public void setTransfer_origin(String transfer_origin) {
        this.transfer_origin = transfer_origin;
    }

    public int getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(int origin_id) {
        this.origin_id = origin_id;
    }

    public String getSim_flg() {
        return sim_flg;
    }

    public void setSim_flg(String sim_flg) {
        this.sim_flg = sim_flg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
}
