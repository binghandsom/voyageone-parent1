package com.voyageone.components.overstock.bean.variation;

import com.voyageone.components.overstock.bean.OverstockMultipleRequest;

/**
 * @author aooer 2016/6/7.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OverstockVariationMultipleQueryRequest extends OverstockMultipleRequest {

    private String listingId;//QueryParameter retailerProperties.ListingId

    private String inventoryOverride;//QueryParameter retailerProperties.InventoryOverride

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getInventoryOverride() {
        return inventoryOverride;
    }

    public void setInventoryOverride(String inventoryOverride) {
        this.inventoryOverride = inventoryOverride;
    }
}
