package com.voyageone.components.onestop.bean;

/**
 * @description
 * @author: zhen.wang
 * @date: 2016/11/22 11:06
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopInventory {

    /*
    *   UPC: string
    *   Units: 0
    *   InventoryTypeID: 0
    *
    * */

    private String UPC;
    private String sku;
    private int Units;

    private int InventoryTypeID;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public String getUPC() {
        return this.UPC;
    }

    public void setUnits(int Units) {
        this.Units = Units;
    }

    public int getUnits() {
        return this.Units;
    }

    public void setInventoryTypeID(int InventoryTypeID) {
        this.InventoryTypeID = InventoryTypeID;
    }

    public int getInventoryTypeID() {
        return this.InventoryTypeID;
    }


}
