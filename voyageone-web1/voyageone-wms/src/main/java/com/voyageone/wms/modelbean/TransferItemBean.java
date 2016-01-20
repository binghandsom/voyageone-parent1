package com.voyageone.wms.modelbean;

/**
 * Created by Tester on 4/30/2015.
 *
 * @author Jonas
 */
public class TransferItemBean {
    private long transfer_item_id;
    private long transfer_id;
    private long transfer_package_id;
    private String transfer_barcode;
    private String transfer_sku;
    private int transfer_qty;
    private long order_number;
    private long reservation_id;
    private int calc_qty;
    private String syn_flg;
    private boolean active;
    private String created;
    private String creater;
    private String modified;
    private String modifier;

    public long getTransfer_item_id() {
        return transfer_item_id;
    }

    public void setTransfer_item_id(long transfer_item_id) {
        this.transfer_item_id = transfer_item_id;
    }

    public long getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public long getTransfer_package_id() {
        return transfer_package_id;
    }

    public void setTransfer_package_id(long transfer_package_id) {
        this.transfer_package_id = transfer_package_id;
    }

    public String getTransfer_barcode() {
        return transfer_barcode;
    }

    public void setTransfer_barcode(String transfer_barcode) {
        this.transfer_barcode = transfer_barcode;
    }

    public String getTransfer_sku() {
        return transfer_sku;
    }

    public void setTransfer_sku(String transfer_sku) {
        this.transfer_sku = transfer_sku;
    }

    public int getTransfer_qty() {
        return transfer_qty;
    }

    public void setTransfer_qty(int transfer_qty) {
        this.transfer_qty = transfer_qty;
    }

    public long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(long order_number) {
        this.order_number = order_number;
    }

    public long getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(long reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getCalc_qty() {
        return calc_qty;
    }

    public void setCalc_qty(int calc_qty) {
        this.calc_qty = calc_qty;
    }

    public String getSyn_flg() {
        return syn_flg;
    }

    public void setSyn_flg(String syn_flg) {
        this.syn_flg = syn_flg;
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
