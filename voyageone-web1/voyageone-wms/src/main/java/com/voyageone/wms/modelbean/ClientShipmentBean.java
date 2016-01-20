package com.voyageone.wms.modelbean;

/**
 * @author jack
 * ClientShipmentBean
 */
public class ClientShipmentBean {

    private long shipment_id;
    private String order_channel_id;
    private String file_name;
    private String brand;
    private long transfer_id;
    private String syn_flg;
    private String active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public long getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(long shipment_id) {
        this.shipment_id = shipment_id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public String getSyn_flg() {
        return syn_flg;
    }

    public void setSyn_flg(String syn_flg) {
        this.syn_flg = syn_flg;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
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
