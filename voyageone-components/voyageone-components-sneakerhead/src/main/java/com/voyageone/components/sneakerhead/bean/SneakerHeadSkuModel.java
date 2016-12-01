package com.voyageone.components.sneakerhead.bean;

import java.util.Date;

/**
 * SneakerHeadSkuModel
 *
 * @author vantis
 * @version 1.0.0
 * @since 1.0.0
 */
public class SneakerHeadSkuModel {

    private String sku;
    private String barcode;
    private String quantity;
    private String size;
    private Date created;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
